package com.manju.repolens.dto;

import com.manju.repolens.model.NodeType;

public record GraphNodeResponse(
    Long id,
    String key,
    NodeType type,
    String name,
    String qualifiedName,
    String filePath,
    Integer lineStart,
    Integer lineEnd,
    String metadata
) {
}
