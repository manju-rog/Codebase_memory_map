package com.manju.repolens.service.ask;

import com.manju.repolens.dto.AskQuestionRequest;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.exception.ResourceNotFoundException;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.repository.RepoScanRepository;
import com.manju.repolens.service.graph.GraphSearchService;
import com.manju.repolens.service.graph.ImpactAnalysisService;
import com.manju.repolens.service.graph.MermaidGraphExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionAnswerService {
    
    private final RepoScanRepository scanRepository;
    private final GraphSearchService graphSearchService;
    private final ImpactAnalysisService impactAnalysisService;
    private final MermaidGraphExporter mermaidExporter;
    private final GraphContextBuilder contextBuilder;
    private final FallbackAnswerService fallbackAnswerService;
    
    @Autowired(required = false)
    private AiAnswerService aiAnswerService;
    
    public QuestionAnswerService(
            RepoScanRepository scanRepository,
            GraphSearchService graphSearchService,
            ImpactAnalysisService impactAnalysisService,
            MermaidGraphExporter mermaidExporter,
            GraphContextBuilder contextBuilder,
            FallbackAnswerService fallbackAnswerService) {
        this.scanRepository = scanRepository;
        this.graphSearchService = graphSearchService;
        this.impactAnalysisService = impactAnalysisService;
        this.mermaidExporter = mermaidExporter;
        this.contextBuilder = contextBuilder;
        this.fallbackAnswerService = fallbackAnswerService;
    }
    
    public AskQuestionResponse answerQuestion(Long scanId, AskQuestionRequest request) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        String question = request.question();
        
        // Search for relevant nodes
        List<CodeNode> relevantNodes = graphSearchService.searchRelevantNodes(scan, question);
        
        // Find affected nodes
        Set<CodeNode> affectedNodes = impactAnalysisService.findAffectedNodes(relevantNodes, 2);
        
        // Generate Mermaid graph
        String mermaidGraph = generateMermaidForNodes(relevantNodes);
        
        // Build context
        String graphContext = contextBuilder.buildContext(relevantNodes);
        
        // Generate fallback answer
        AskQuestionResponse fallbackAnswer = fallbackAnswerService.generateFallbackAnswer(
            question, relevantNodes, affectedNodes, mermaidGraph);
        
        // Enhance with AI if available
        if (aiAnswerService != null) {
            return aiAnswerService.enhanceAnswer(question, graphContext, fallbackAnswer);
        }
        
        return fallbackAnswer;
    }
    
    private String generateMermaidForNodes(List<CodeNode> nodes) {
        if (nodes.isEmpty()) {
            return "graph TD\n  A[No relevant nodes found]";
        }
        
        StringBuilder mermaid = new StringBuilder("graph TD\n");
        for (int i = 0; i < Math.min(nodes.size(), 5); i++) {
            CodeNode node = nodes.get(i);
            mermaid.append("  N").append(i)
                   .append("[\"").append(node.getName()).append("\"]\n");
            if (i > 0) {
                mermaid.append("  N").append(i - 1).append(" --> N").append(i).append("\n");
            }
        }
        
        return mermaid.toString();
    }
}
