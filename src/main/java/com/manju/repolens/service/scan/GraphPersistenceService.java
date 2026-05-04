package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.repository.CodeEdgeRepository;
import com.manju.repolens.repository.CodeNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GraphPersistenceService {
    
    private final CodeNodeRepository nodeRepository;
    private final CodeEdgeRepository edgeRepository;
    
    public GraphPersistenceService(CodeNodeRepository nodeRepository, CodeEdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }
    
    @Transactional
    public void saveGraph(List<CodeNode> nodes, List<CodeEdge> edges) {
        nodeRepository.saveAll(nodes);
        edgeRepository.saveAll(edges);
    }
}
