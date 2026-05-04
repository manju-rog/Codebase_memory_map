package com.manju.repolens.service.graph;

import com.manju.repolens.dto.*;
import com.manju.repolens.exception.ResourceNotFoundException;
import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.repository.CodeEdgeRepository;
import com.manju.repolens.repository.CodeNodeRepository;
import com.manju.repolens.repository.RepoScanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphQueryService {
    
    private final RepoScanRepository scanRepository;
    private final CodeNodeRepository nodeRepository;
    private final CodeEdgeRepository edgeRepository;
    
    public GraphQueryService(RepoScanRepository scanRepository, 
                            CodeNodeRepository nodeRepository,
                            CodeEdgeRepository edgeRepository) {
        this.scanRepository = scanRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }
    
    public ScanStatusResponse getScanStatus(Long scanId) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        return new ScanStatusResponse(
            scan.getId(),
            scan.getStatus(),
            scan.getRepoName(),
            scan.getStartedAt(),
            scan.getCompletedAt(),
            scan.getTotalFilesScanned(),
            scan.getTotalNodes(),
            scan.getTotalEdges(),
            scan.getErrorMessage()
        );
    }
    
    public GraphResponse getGraph(Long scanId) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        List<CodeNode> nodes = nodeRepository.findByScan(scan);
        List<CodeEdge> edges = edgeRepository.findByScan(scan);
        
        List<GraphNodeResponse> nodeResponses = nodes.stream()
            .map(this::toNodeResponse)
            .collect(Collectors.toList());
        
        List<GraphEdgeResponse> edgeResponses = edges.stream()
            .map(this::toEdgeResponse)
            .collect(Collectors.toList());
        
        return new GraphResponse(scan.getId(), scan.getRepoName(), nodeResponses, edgeResponses);
    }
    
    public List<GraphNodeResponse> getNodesByType(Long scanId, NodeType type) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        List<CodeNode> nodes = nodeRepository.findByScanAndType(scan, type);
        
        return nodes.stream()
            .map(this::toNodeResponse)
            .collect(Collectors.toList());
    }
    
    public List<GraphNodeResponse> searchNodes(Long scanId, String query) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        List<CodeNode> nodes = nodeRepository.searchNodes(scan, query);
        
        return nodes.stream()
            .map(this::toNodeResponse)
            .collect(Collectors.toList());
    }
    
    private GraphNodeResponse toNodeResponse(CodeNode node) {
        return new GraphNodeResponse(
            node.getId(),
            node.getNodeKey(),
            node.getType(),
            node.getName(),
            node.getQualifiedName(),
            node.getFilePath(),
            node.getLineStart(),
            node.getLineEnd(),
            node.getMetadataJson()
        );
    }
    
    private GraphEdgeResponse toEdgeResponse(CodeEdge edge) {
        return new GraphEdgeResponse(
            edge.getId(),
            edge.getSourceNode().getId(),
            edge.getTargetNode().getId(),
            edge.getType(),
            edge.getConfidence(),
            edge.getReason()
        );
    }
}
