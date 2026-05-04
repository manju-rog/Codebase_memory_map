package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.EdgeType;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RelationshipResolver {
    
    public List<CodeEdge> resolveRelationships(
            ParsedJavaFile parsed, 
            CodeNode sourceNode, 
            Map<String, CodeNode> nodesByKey,
            RepoScan scan) {
        
        List<CodeEdge> edges = new ArrayList<>();
        
        // Constructor injection creates INJECTS edges
        for (String paramType : parsed.constructorParamTypes) {
            String targetKey = findNodeKeyByClassName(paramType, nodesByKey);
            if (targetKey != null) {
                CodeNode targetNode = nodesByKey.get(targetKey);
                edges.add(new CodeEdge(scan, sourceNode, targetNode, EdgeType.INJECTS, 
                    "Constructor injection of " + paramType));
            }
        }
        
        // Field types create USES edges
        for (String fieldType : parsed.fieldTypes) {
            String targetKey = findNodeKeyByClassName(fieldType, nodesByKey);
            if (targetKey != null) {
                CodeNode targetNode = nodesByKey.get(targetKey);
                if (!hasEdge(edges, sourceNode, targetNode, EdgeType.INJECTS)) {
                    edges.add(new CodeEdge(scan, sourceNode, targetNode, EdgeType.USES, 
                        "Uses " + fieldType + " as field"));
                }
            }
        }
        
        return edges;
    }
    
    public List<CodeEdge> resolveRepositoryToEntity(
            CodeNode repositoryNode,
            String entityType,
            Map<String, CodeNode> nodesByKey,
            RepoScan scan) {
        
        List<CodeEdge> edges = new ArrayList<>();
        String entityKey = findNodeKeyByClassName(entityType, nodesByKey);
        
        if (entityKey != null) {
            CodeNode entityNode = nodesByKey.get(entityKey);
            edges.add(new CodeEdge(scan, repositoryNode, entityNode, EdgeType.PERSISTS_TO,
                "Repository manages " + entityType + " entity"));
        }
        
        return edges;
    }
    
    public List<CodeEdge> resolveEntityToTable(
            CodeNode entityNode,
            CodeNode tableNode,
            RepoScan scan) {
        
        List<CodeEdge> edges = new ArrayList<>();
        edges.add(new CodeEdge(scan, entityNode, tableNode, EdgeType.MAPS_TO_TABLE,
            "Entity maps to database table"));
        return edges;
    }
    
    public List<CodeEdge> resolveControllerToEndpoints(
            CodeNode controllerNode,
            List<CodeNode> endpoints,
            RepoScan scan) {
        
        List<CodeEdge> edges = new ArrayList<>();
        for (CodeNode endpoint : endpoints) {
            edges.add(new CodeEdge(scan, controllerNode, endpoint, EdgeType.EXPOSES_ENDPOINT,
                "Controller exposes endpoint"));
        }
        return edges;
    }
    
    private String findNodeKeyByClassName(String className, Map<String, CodeNode> nodesByKey) {
        // Try exact match first
        String exactKey = "class:" + className;
        if (nodesByKey.containsKey(exactKey)) {
            return exactKey;
        }
        
        // Try simple name match
        for (Map.Entry<String, CodeNode> entry : nodesByKey.entrySet()) {
            if (entry.getValue().getName().equals(className)) {
                return entry.getKey();
            }
        }
        
        return null;
    }
    
    private boolean hasEdge(List<CodeEdge> edges, CodeNode source, CodeNode target, EdgeType type) {
        return edges.stream().anyMatch(e -> 
            e.getSourceNode().equals(source) && 
            e.getTargetNode().equals(target) && 
            e.getType() == type
        );
    }
}
