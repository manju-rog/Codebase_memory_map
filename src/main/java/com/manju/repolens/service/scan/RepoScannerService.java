package com.manju.repolens.service.scan;

import com.manju.repolens.dto.ScanRepositoryRequest;
import com.manju.repolens.exception.BadRequestException;
import com.manju.repolens.model.*;
import com.manju.repolens.repository.RepoScanRepository;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import com.manju.repolens.util.NameNormalizer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Instant;
import java.util.*;

@Service
public class RepoScannerService {
    
    private final RepoScanRepository scanRepository;
    private final GitCloneService gitCloneService;
    private final LocalRepoValidationService localRepoValidationService;
    private final FileDiscoveryService fileDiscoveryService;
    private final JavaSourceParserService javaParserService;
    private final SpringComponentExtractor componentExtractor;
    private final EndpointExtractor endpointExtractor;
    private final EntityExtractor entityExtractor;
    private final RepositoryExtractor repositoryExtractor;
    private final PomDependencyExtractor pomExtractor;
    private final ReadmeExtractor readmeExtractor;
    private final RelationshipResolver relationshipResolver;
    private final GraphPersistenceService graphPersistenceService;
    
    public RepoScannerService(
            RepoScanRepository scanRepository,
            GitCloneService gitCloneService,
            LocalRepoValidationService localRepoValidationService,
            FileDiscoveryService fileDiscoveryService,
            JavaSourceParserService javaParserService,
            SpringComponentExtractor componentExtractor,
            EndpointExtractor endpointExtractor,
            EntityExtractor entityExtractor,
            RepositoryExtractor repositoryExtractor,
            PomDependencyExtractor pomExtractor,
            ReadmeExtractor readmeExtractor,
            RelationshipResolver relationshipResolver,
            GraphPersistenceService graphPersistenceService) {
        this.scanRepository = scanRepository;
        this.gitCloneService = gitCloneService;
        this.localRepoValidationService = localRepoValidationService;
        this.fileDiscoveryService = fileDiscoveryService;
        this.javaParserService = javaParserService;
        this.componentExtractor = componentExtractor;
        this.endpointExtractor = endpointExtractor;
        this.entityExtractor = entityExtractor;
        this.repositoryExtractor = repositoryExtractor;
        this.pomExtractor = pomExtractor;
        this.readmeExtractor = readmeExtractor;
        this.relationshipResolver = relationshipResolver;
        this.graphPersistenceService = graphPersistenceService;
    }
    
    @Transactional
    public RepoScan initiateScan(ScanRepositoryRequest request) {
        validateRequest(request);
        
        String repoName = determineRepoName(request);
        
        RepoScan scan = new RepoScan(
            request.sourceType(),
            repoName,
            request.repoUrl(),
            request.localPath(),
            request.getBranch()
        );
        
        scan = scanRepository.save(scan);
        
        // Start async scan
        performScanAsync(scan.getId(), request);
        
        return scan;
    }
    
    @Async
    public void performScanAsync(Long scanId, ScanRepositoryRequest request) {
        RepoScan scan = scanRepository.findById(scanId).orElseThrow();
        scan.setStatus(ScanStatus.RUNNING);
        scanRepository.save(scan);
        
        File scanDir = null;
        boolean shouldCleanup = false;
        
        try {
            if (request.sourceType() == SourceType.GITHUB) {
                scanDir = gitCloneService.cloneRepository(request.repoUrl(), request.getBranch());
                shouldCleanup = true;
            } else {
                scanDir = localRepoValidationService.validateAndGetDirectory(request.localPath());
            }
            
            performScan(scan, scanDir);
            
            scan.setStatus(ScanStatus.COMPLETED);
            scan.setCompletedAt(Instant.now());
            
        } catch (Exception e) {
            scan.setStatus(ScanStatus.FAILED);
            scan.setErrorMessage(e.getMessage());
            scan.setCompletedAt(Instant.now());
        } finally {
            scanRepository.save(scan);
            if (shouldCleanup && scanDir != null) {
                gitCloneService.cleanupDirectory(scanDir);
            }
        }
    }
    
    private void performScan(RepoScan scan, File scanDir) {
        List<File> files = fileDiscoveryService.discoverFiles(scanDir);
        scan.setTotalFilesScanned(files.size());
        
        List<CodeNode> allNodes = new ArrayList<>();
        List<CodeEdge> allEdges = new ArrayList<>();
        Map<String, CodeNode> nodesByKey = new HashMap<>();
        Map<String, ParsedJavaFile> parsedFiles = new HashMap<>();
        
        // Phase 1: Extract nodes from Java files
        for (File file : files) {
            if (file.getName().endsWith(".java")) {
                Optional<ParsedJavaFile> parsed = javaParserService.parseJavaFile(file);
                if (parsed.isPresent()) {
                    ParsedJavaFile pf = parsed.get();
                    parsedFiles.put(pf.qualifiedName, pf);
                    
                    Optional<CodeNode> componentNode = componentExtractor.extractComponent(pf, file, scanDir, scan);
                    if (componentNode.isPresent()) {
                        CodeNode node = componentNode.get();
                        allNodes.add(node);
                        nodesByKey.put(node.getNodeKey(), node);
                        
                        // Extract endpoints if controller
                        List<CodeNode> endpoints = endpointExtractor.extractEndpoints(pf, node, scan);
                        allNodes.addAll(endpoints);
                        endpoints.forEach(ep -> nodesByKey.put(ep.getNodeKey(), ep));
                        allEdges.addAll(relationshipResolver.resolveControllerToEndpoints(node, endpoints, scan));
                        
                        // Extract table if entity
                        Optional<CodeNode> tableNode = entityExtractor.extractDatabaseTable(pf, scan);
                        if (tableNode.isPresent()) {
                            CodeNode table = tableNode.get();
                            allNodes.add(table);
                            nodesByKey.put(table.getNodeKey(), table);
                            allEdges.addAll(relationshipResolver.resolveEntityToTable(node, table, scan));
                        }
                    }
                }
            }
        }
        
        // Phase 2: Extract dependencies and README
        for (File file : files) {
            if (file.getName().equals("pom.xml")) {
                List<CodeNode> deps = pomExtractor.extractDependencies(file, scan);
                allNodes.addAll(deps);
                deps.forEach(dep -> nodesByKey.put(dep.getNodeKey(), dep));
            } else if (file.getName().equalsIgnoreCase("README.md")) {
                List<CodeNode> sections = readmeExtractor.extractSections(file, scan);
                allNodes.addAll(sections);
                sections.forEach(sec -> nodesByKey.put(sec.getNodeKey(), sec));
            }
        }
        
        // Phase 3: Resolve relationships
        for (Map.Entry<String, ParsedJavaFile> entry : parsedFiles.entrySet()) {
            String qualifiedName = entry.getKey();
            ParsedJavaFile pf = entry.getValue();
            String nodeKey = "class:" + qualifiedName;
            CodeNode sourceNode = nodesByKey.get(nodeKey);
            
            if (sourceNode != null) {
                allEdges.addAll(relationshipResolver.resolveRelationships(pf, sourceNode, nodesByKey, scan));
                
                // Repository to Entity
                if (sourceNode.getType() == NodeType.REPOSITORY_INTERFACE) {
                    Optional<String> entityType = repositoryExtractor.extractEntityType(pf);
                    if (entityType.isPresent()) {
                        allEdges.addAll(relationshipResolver.resolveRepositoryToEntity(
                            sourceNode, entityType.get(), nodesByKey, scan));
                    }
                }
            }
        }
        
        // Save to database
        graphPersistenceService.saveGraph(allNodes, allEdges);
        
        scan.setTotalNodes(allNodes.size());
        scan.setTotalEdges(allEdges.size());
    }
    
    private void validateRequest(ScanRepositoryRequest request) {
        if (request.sourceType() == SourceType.GITHUB) {
            if (request.repoUrl() == null || request.repoUrl().isBlank()) {
                throw new BadRequestException("repoUrl is required when sourceType is GITHUB");
            }
        } else if (request.sourceType() == SourceType.LOCAL) {
            if (request.localPath() == null || request.localPath().isBlank()) {
                throw new BadRequestException("localPath is required when sourceType is LOCAL");
            }
        }
    }
    
    private String determineRepoName(ScanRepositoryRequest request) {
        if (request.sourceType() == SourceType.GITHUB) {
            return NameNormalizer.extractRepoName(request.repoUrl());
        } else {
            File dir = new File(request.localPath());
            return dir.getName();
        }
    }
}
