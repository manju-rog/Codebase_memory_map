package com.manju.repolens.dto;

import com.manju.repolens.model.SourceType;
import jakarta.validation.constraints.NotNull;

public record ScanRepositoryRequest(
    @NotNull(message = "sourceType is required")
    SourceType sourceType,
    
    String repoUrl,
    
    String localPath,
    
    String branch
) {
    public String getBranch() {
        return branch != null && !branch.isBlank() ? branch : "main";
    }
}
