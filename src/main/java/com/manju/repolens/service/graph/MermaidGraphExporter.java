package com.manju.repolens.service.graph;

import com.manju.repolens.exception.ResourceNotFoundException;
import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.repository.CodeEdgeRepository;
import com.manju.repolens.repository.CodeNodeRepository;
import com.manju.repolens.repository.RepoScanRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MermaidGraphExporter {
    
    private final RepoScanRepository scanRepository;
    private final CodeNodeRepository nodeRepository;
    private final CodeEdgeRepository edgeRepository;
    
    public MermaidGraphExporter(RepoScanRepository scanRepository,
                               CodeNodeRepository nodeRepository,
                               CodeEdgeRepository edgeRepository) {
        this.scanRepository = scanRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }
    
    public String exportToMermaid(Long scanId) {
        RepoScan scan = scanRepository.findById(scanId)
            .orElseThrow(() -> new ResourceNotFoundException("Scan not found: " + scanId));
        
        List<CodeNode> nodes = nodeRepository.findByScan(scan);
        List<CodeEdge> edges = edgeRepository.findByScan(scan);
        
        StringBuilder mermaid = new StringBuilder();
        mermaid.append("graph TD\n");
        
        Map<Long, String> nodeIdMap = new HashMap<>();
        int counter = 1;
        
        for (CodeNode node : nodes) {
            String nodeId = "N" + counter++;
            nodeIdMap.put(node.getId(), nodeId);
            mermaid.append("  ").append(nodeId)
                   .append("[\"").append(sanitize(node.getName())).append("\"]\n");
        }
        
        for (CodeEdge edge : edges) {
            String sourceId = nodeIdMap.get(edge.getSourceNode().getId());
            String targetId = nodeIdMap.get(edge.getTargetNode().getId());
            
            if (sourceId != null && targetId != null) {
                mermaid.append("  ").append(sourceId)
                       .append(" --> ")
                       .append(targetId)
                       .append("\n");
            }
        }
        
        return mermaid.toString();
    }
    
    private String sanitize(String text) {
        if (text == null) return "";
        return text.replace("\"", "'");
    }
}
