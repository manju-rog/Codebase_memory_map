package com.manju.repolens.controller;

import com.manju.repolens.dto.GraphNodeResponse;
import com.manju.repolens.dto.GraphResponse;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.service.graph.GraphQueryService;
import com.manju.repolens.service.graph.MermaidGraphExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repos/scans/{scanId}")
@Tag(name = "Graph Query", description = "APIs for querying the codebase knowledge graph")
public class GraphController {
    
    private final GraphQueryService graphQueryService;
    private final MermaidGraphExporter mermaidExporter;
    
    public GraphController(GraphQueryService graphQueryService, MermaidGraphExporter mermaidExporter) {
        this.graphQueryService = graphQueryService;
        this.mermaidExporter = mermaidExporter;
    }
    
    @GetMapping("/graph")
    @Operation(summary = "Get full graph", description = "Retrieves the complete knowledge graph for a scan")
    public ResponseEntity<GraphResponse> getGraph(@PathVariable Long scanId) {
        GraphResponse response = graphQueryService.getGraph(scanId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/graph/mermaid")
    @Operation(summary = "Get Mermaid diagram", description = "Exports the graph as a Mermaid diagram")
    public ResponseEntity<String> getMermaidGraph(@PathVariable Long scanId) {
        String mermaid = mermaidExporter.exportToMermaid(scanId);
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .body(mermaid);
    }
    
    @GetMapping("/nodes")
    @Operation(summary = "Get nodes by type", description = "Retrieves nodes filtered by type")
    public ResponseEntity<List<GraphNodeResponse>> getNodesByType(
            @PathVariable Long scanId,
            @RequestParam NodeType type) {
        List<GraphNodeResponse> nodes = graphQueryService.getNodesByType(scanId, type);
        return ResponseEntity.ok(nodes);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search nodes", description = "Searches for nodes matching a query")
    public ResponseEntity<List<GraphNodeResponse>> searchNodes(
            @PathVariable Long scanId,
            @RequestParam String q) {
        List<GraphNodeResponse> nodes = graphQueryService.searchNodes(scanId, q);
        return ResponseEntity.ok(nodes);
    }
}
