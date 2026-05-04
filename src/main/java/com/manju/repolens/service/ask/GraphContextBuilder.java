package com.manju.repolens.service.ask;

import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.repository.CodeEdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphContextBuilder {
    
    private final CodeEdgeRepository edgeRepository;
    
    public GraphContextBuilder(CodeEdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }
    
    public String buildContext(List<CodeNode> nodes) {
        StringBuilder context = new StringBuilder();
        
        for (CodeNode node : nodes) {
            context.append("- ").append(node.getType()).append(": ")
                   .append(node.getName());
            
            if (node.getFilePath() != null) {
                context.append(" (").append(node.getFilePath()).append(")");
            }
            
            context.append("\n");
            
            List<CodeEdge> edges = edgeRepository.findBySourceNodeOrTargetNode(node, node);
            for (CodeEdge edge : edges.stream().limit(3).collect(Collectors.toList())) {
                String direction = edge.getSourceNode().equals(node) ? "->" : "<-";
                CodeNode other = edge.getSourceNode().equals(node) ? 
                    edge.getTargetNode() : edge.getSourceNode();
                
                context.append("  ").append(direction).append(" ")
                       .append(edge.getType()).append(" ")
                       .append(other.getName()).append("\n");
            }
        }
        
        return context.toString();
    }
}
