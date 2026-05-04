package com.manju.repolens.service.graph;

import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.repository.CodeEdgeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImpactAnalysisService {
    
    private final CodeEdgeRepository edgeRepository;
    
    public ImpactAnalysisService(CodeEdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }
    
    public Set<CodeNode> findAffectedNodes(List<CodeNode> startNodes, int maxDepth) {
        Set<CodeNode> affected = new HashSet<>(startNodes);
        Set<CodeNode> visited = new HashSet<>();
        
        for (CodeNode startNode : startNodes) {
            traverseGraph(startNode, affected, visited, 0, maxDepth);
        }
        
        return affected;
    }
    
    private void traverseGraph(CodeNode node, Set<CodeNode> affected, Set<CodeNode> visited, 
                              int currentDepth, int maxDepth) {
        if (currentDepth >= maxDepth || visited.contains(node)) {
            return;
        }
        
        visited.add(node);
        
        List<CodeEdge> outgoing = edgeRepository.findBySourceNode(node);
        List<CodeEdge> incoming = edgeRepository.findByTargetNode(node);
        
        for (CodeEdge edge : outgoing) {
            CodeNode target = edge.getTargetNode();
            affected.add(target);
            traverseGraph(target, affected, visited, currentDepth + 1, maxDepth);
        }
        
        for (CodeEdge edge : incoming) {
            CodeNode source = edge.getSourceNode();
            affected.add(source);
            traverseGraph(source, affected, visited, currentDepth + 1, maxDepth);
        }
    }
}
