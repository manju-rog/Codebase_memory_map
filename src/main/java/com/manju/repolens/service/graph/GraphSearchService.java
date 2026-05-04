package com.manju.repolens.service.graph;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.repository.CodeNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphSearchService {
    
    private final CodeNodeRepository nodeRepository;
    
    public GraphSearchService(CodeNodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }
    
    public List<CodeNode> searchRelevantNodes(RepoScan scan, String query) {
        return nodeRepository.searchNodes(scan, query);
    }
}
