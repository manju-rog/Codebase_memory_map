package com.manju.repolens.dto;

import java.util.List;

public record GraphResponse(
    Long scanId,
    String repoName,
    List<GraphNodeResponse> nodes,
    List<GraphEdgeResponse> edges
) {
}
