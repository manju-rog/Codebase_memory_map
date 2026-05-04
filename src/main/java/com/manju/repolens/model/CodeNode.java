package com.manju.repolens.model;

import jakarta.persistence.*;

@Entity
@Table(name = "code_nodes", indexes = {
    @Index(name = "idx_scan_id", columnList = "scan_id"),
    @Index(name = "idx_node_type", columnList = "type"),
    @Index(name = "idx_node_name", columnList = "name"),
    @Index(name = "idx_node_key", columnList = "nodeKey")
})
public class CodeNode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scan_id", nullable = false)
    private RepoScan scan;
    
    @Column(nullable = false, length = 500)
    private String nodeKey;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NodeType type;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 500)
    private String qualifiedName;
    
    @Column(length = 1000)
    private String filePath;
    
    private Integer lineStart;
    
    private Integer lineEnd;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String metadataJson;
    
    public CodeNode() {
    }
    
    public CodeNode(RepoScan scan, String nodeKey, NodeType type, String name) {
        this.scan = scan;
        this.nodeKey = nodeKey;
        this.type = type;
        this.name = name;
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
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public NodeType getType() {
        return type;
    }
    
    public void setType(NodeType type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getQualifiedName() {
        return qualifiedName;
    }
    
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Integer getLineStart() {
        return lineStart;
    }
    
    public void setLineStart(Integer lineStart) {
        this.lineStart = lineStart;
    }
    
    public Integer getLineEnd() {
        return lineEnd;
    }
    
    public void setLineEnd(Integer lineEnd) {
        this.lineEnd = lineEnd;
    }
    
    public String getMetadataJson() {
        return metadataJson;
    }
    
    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }
}
