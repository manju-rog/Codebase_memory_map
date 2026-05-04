package com.manju.repolens.service.ask;

import com.manju.repolens.dto.AffectedFileResponse;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.dto.EvidenceResponse;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FallbackAnswerService {
    
    public AskQuestionResponse generateFallbackAnswer(
            String question, 
            List<CodeNode> relevantNodes,
            Set<CodeNode> affectedNodes,
            String mermaidGraph) {
        
        String answer = buildAnswer(question, relevantNodes);
        List<EvidenceResponse> evidence = buildEvidence(relevantNodes);
        List<AffectedFileResponse> affectedFiles = buildAffectedFiles(affectedNodes);
        double confidence = calculateConfidence(relevantNodes);
        
        return new AskQuestionResponse(answer, confidence, evidence, affectedFiles, mermaidGraph);
    }
    
    private String buildAnswer(String question, List<CodeNode> nodes) {
        if (nodes.isEmpty()) {
            return "No relevant code found for: " + question;
        }
        
        StringBuilder answer = new StringBuilder();
        
        List<CodeNode> controllers = filterByType(nodes, NodeType.CONTROLLER);
        List<CodeNode> services = filterByType(nodes, NodeType.SERVICE);
        List<CodeNode> repositories = filterByType(nodes, NodeType.REPOSITORY_INTERFACE);
        List<CodeNode> entities = filterByType(nodes, NodeType.ENTITY);
        List<CodeNode> endpoints = filterByType(nodes, NodeType.ENDPOINT);
        List<CodeNode> tables = filterByType(nodes, NodeType.DATABASE_TABLE);
        
        if (!controllers.isEmpty()) {
            answer.append("Found in controller: ").append(controllers.get(0).getName()).append(". ");
        }
        
        if (!endpoints.isEmpty()) {
            answer.append("Exposes endpoint: ").append(endpoints.get(0).getName()).append(". ");
        }
        
        if (!services.isEmpty()) {
            answer.append("Uses service: ").append(services.get(0).getName()).append(". ");
        }
        
        if (!repositories.isEmpty()) {
            answer.append("Accesses repository: ").append(repositories.get(0).getName()).append(". ");
        }
        
        if (!entities.isEmpty()) {
            answer.append("Works with entity: ").append(entities.get(0).getName()).append(". ");
        }
        
        if (!tables.isEmpty()) {
            answer.append("Maps to table: ").append(tables.get(0).getName()).append(".");
        }
        
        return answer.toString();
    }
    
    private List<EvidenceResponse> buildEvidence(List<CodeNode> nodes) {
        return nodes.stream()
            .limit(5)
            .map(node -> new EvidenceResponse(
                node.getFilePath(),
                node.getQualifiedName(),
                node.getType(),
                node.getLineStart(),
                node.getLineEnd(),
                "Matched search query"
            ))
            .collect(Collectors.toList());
    }
    
    private List<AffectedFileResponse> buildAffectedFiles(Set<CodeNode> nodes) {
        return nodes.stream()
            .filter(node -> node.getFilePath() != null)
            .map(node -> new AffectedFileResponse(
                node.getFilePath(),
                node.getType().toString() + ": " + node.getName()
            ))
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
    }
    
    private double calculateConfidence(List<CodeNode> nodes) {
        if (nodes.isEmpty()) return 0.0;
        if (nodes.size() >= 3) return 0.85;
        if (nodes.size() == 2) return 0.70;
        return 0.50;
    }
    
    private List<CodeNode> filterByType(List<CodeNode> nodes, NodeType type) {
        return nodes.stream()
            .filter(n -> n.getType() == type)
            .collect(Collectors.toList());
    }
}
