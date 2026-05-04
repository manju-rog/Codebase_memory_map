package com.manju.repolens.model;

import jakarta.persistence.*;

@Entity
@Table(name = "code_edges", indexes = {
    @Index(name = "idx_edge_scan", columnList = "scan_id"),
    @Index(name = "idx_edge_source", columnList = "source_node_id"),
    @Index(name = "idx_edge_target", columnList = "target_node_id")
})
public class CodeEdge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scan_id", nullable = false)
    private RepoScan scan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_node_id", nullable = false)
    private CodeNode sourceNode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_node_id", nullable = false)
    private CodeNode targetNode;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EdgeType type;
    
    private Double confidence;
    
    @Column(length = 500)
    private String reason;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String metadataJson;
    
    public CodeEdge() {
    }
    
    public CodeEdge(RepoScan scan, CodeNode sourceNode, CodeNode targetNode, EdgeType type, String reason) {
        this.scan = scan;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.type = type;
        this.reason = reason;
        this.confidence = 1.0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public RepoScan getScan() {
        return scan;
    }
    
    public void setScan(RepoScan scan) {
        this.scan = scan;
    }
    
    public CodeNode getSourceNode() {
        return sourceNode;
    }
    
    public void setSourceNode(CodeNode sourceNode) {
        this.sourceNode = sourceNode;
    }
    
    public CodeNode getTargetNode() {
        return targetNode;
    }
    
    public void setTargetNode(CodeNode targetNode) {
        this.targetNode = targetNode;
    }
    
    public EdgeType getType() {
        return type;
    }
    
    public void setType(EdgeType type) {
        this.type = type;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getMetadataJson() {
        return metadataJson;
    }
    
    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }
}
