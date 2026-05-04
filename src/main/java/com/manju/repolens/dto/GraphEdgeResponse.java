package com.manju.repolens.dto;

import com.manju.repolens.model.EdgeType;

public record GraphEdgeResponse(
    Long id,
    Long sourceNodeId,
    Long targetNodeId,
    EdgeType type,
    Double confidence,
    String reason
) {
}
