package com.manju.repolens.dto;

import com.manju.repolens.model.ScanStatus;

public record ScanRepositoryResponse(
    Long scanId,
    ScanStatus status,
    String repoName,
    String branch,
    int totalFilesScanned,
    int totalNodes,
    int totalEdges,
    String message
) {
}
