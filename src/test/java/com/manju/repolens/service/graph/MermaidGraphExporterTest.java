package com.manju.repolens.service.graph;

import com.manju.repolens.model.*;
import com.manju.repolens.repository.CodeEdgeRepository;
import com.manju.repolens.repository.CodeNodeRepository;
import com.manju.repolens.repository.RepoScanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MermaidGraphExporterTest {
    
    @Mock
    private RepoScanRepository scanRepository;
    
    @Mock
    private CodeNodeRepository nodeRepository;
    
    @Mock
    private CodeEdgeRepository edgeRepository;
    
    @InjectMocks
    private MermaidGraphExporter exporter;
    
    @Test
    void shouldExportValidMermaidDiagram() {
        RepoScan scan = new RepoScan(SourceType.LOCAL, "test", null, null, "main");
        scan.setId(1L);
        
        CodeNode node1 = new CodeNode(scan, "key1", NodeType.CONTROLLER, "AuthController");
        node1.setId(1L);
        
        CodeNode node2 = new CodeNode(scan, "key2", NodeType.SERVICE, "AuthService");
        node2.setId(2L);
        
        CodeEdge edge = new CodeEdge(scan, node1, node2, EdgeType.INJECTS, "Injects service");
        
        when(scanRepository.findById(1L)).thenReturn(Optional.of(scan));
        when(nodeRepository.findByScan(scan)).thenReturn(List.of(node1, node2));
        when(edgeRepository.findByScan(scan)).thenReturn(List.of(edge));
        
        String mermaid = exporter.exportToMermaid(1L);
        
        assertNotNull(mermaid);
        assertTrue(mermaid.startsWith("graph TD"));
        assertTrue(mermaid.contains("AuthController"));
        assertTrue(mermaid.contains("AuthService"));
        assertTrue(mermaid.contains("-->"));
    }
}
