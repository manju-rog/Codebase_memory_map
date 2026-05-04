package com.manju.repolens.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "repo_scans")
public class RepoScan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;
    
    @Column(nullable = false)
    private String repoName;
    
    private String repoUrl;
    
    private String localPath;
    
    private String branch;
    
    private String commitHash;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanStatus status;
    
    private Instant startedAt;
    
    private Instant completedAt;
    
    private int totalFilesScanned;
    
    private int totalNodes;
    
    private int totalEdges;
    
    @Column(length = 2000)
    private String errorMessage;
    
    public RepoScan() {
    }
    
    public RepoScan(SourceType sourceType, String repoName, String repoUrl, String localPath, String branch) {
        this.sourceType = sourceType;
        this.repoName = repoName;
        this.repoUrl = repoUrl;
        this.localPath = localPath;
        this.branch = branch;
        this.status = ScanStatus.QUEUED;
        this.startedAt = Instant.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public SourceType getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
    
    public String getRepoName() {
        return repoName;
    }
    
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }
    
    public String getRepoUrl() {
        return repoUrl;
    }
    
    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
    
    public String getLocalPath() {
        return localPath;
    }
    
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getCommitHash() {
        return commitHash;
    }
    
    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }
    
    public ScanStatus getStatus() {
        return status;
    }
    
    public void setStatus(ScanStatus status) {
        this.status = status;
    }
    
    public Instant getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }
    
    public Instant getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
    
    public int getTotalFilesScanned() {
        return totalFilesScanned;
    }
    
    public void setTotalFilesScanned(int totalFilesScanned) {
        this.totalFilesScanned = totalFilesScanned;
    }
    
    public int getTotalNodes() {
        return totalNodes;
    }
    
    public void setTotalNodes(int totalNodes) {
        this.totalNodes = totalNodes;
    }
    
    public int getTotalEdges() {
        return totalEdges;
    }
    
    public void setTotalEdges(int totalEdges) {
        this.totalEdges = totalEdges;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
