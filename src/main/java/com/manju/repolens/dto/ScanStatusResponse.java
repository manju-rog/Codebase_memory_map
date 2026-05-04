package com.manju.repolens.dto;

import com.manju.repolens.model.ScanStatus;
import java.time.Instant;

public record ScanStatusResponse(
    Long scanId,
    ScanStatus status,
    String repoName,
    Instant startedAt,
    Instant completedAt,
    int totalFilesScanned,
    int totalNodes,
    int totalEdges,
    String errorMessage
) {
}
