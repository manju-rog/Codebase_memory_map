package com.manju.repolens.dto;

public record AffectedFileResponse(
    String filePath,
    String reason
) {
}
