package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedMethod;
import com.manju.repolens.util.JsonUtil;
import com.manju.repolens.util.NameNormalizer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EndpointExtractor {
    
    private static final Set<String> MAPPING_ANNOTATIONS = Set.of(
        "GetMapping", "PostMapping", "PutMapping", "DeleteMapping", 
        "PatchMapping", "RequestMapping"
    );
    
    public List<CodeNode> extractEndpoints(ParsedJavaFile parsed, CodeNode controllerNode, RepoScan scan) {
        if (!parsed.annotations.contains("RestController") && !parsed.annotations.contains("Controller")) {
            return Collections.emptyList();
        }
        
        String basePath = extractBasePath(parsed.annotations);
        List<CodeNode> endpoints = new ArrayList<>();
        
        for (ParsedMethod method : parsed.methods) {
            Optional<CodeNode> endpoint = extractEndpoint(method, basePath, parsed.qualifiedName, scan);
            endpoint.ifPresent(endpoints::add);
        }
        
        return endpoints;
    }
    
    private String extractBasePath(List<String> annotations) {
        // Simplified: In real implementation, would parse annotation values
        return "";
    }
    
    private Optional<CodeNode> extractEndpoint(ParsedMethod method, String basePath, 
                                                String className, RepoScan scan) {
        Optional<String> mappingAnnotation = method.annotations.stream()
            .filter(MAPPING_ANNOTATIONS::contains)
            .findFirst();
        
        if (mappingAnnotation.isEmpty()) {
            return Optional.empty();
        }
        
        String httpMethod = extractHttpMethod(mappingAnnotation.get());
        String path = NameNormalizer.combinePaths(basePath, "/" + method.name.toLowerCase());
        
        String nodeKey = "endpoint:" + httpMethod + ":" + path;
        CodeNode endpoint = new CodeNode(scan, nodeKey, NodeType.ENDPOINT, httpMethod + " " + path);
        endpoint.setQualifiedName(className + "." + method.name);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("httpMethod", httpMethod);
        metadata.put("path", path);
        metadata.put("methodName", method.name);
        endpoint.setMetadataJson(JsonUtil.toJson(metadata));
        
        return Optional.of(endpoint);
    }
    
    private String extractHttpMethod(String annotation) {
        return switch (annotation) {
            case "GetMapping" -> "GET";
            case "PostMapping" -> "POST";
            case "PutMapping" -> "PUT";
            case "DeleteMapping" -> "DELETE";
            case "PatchMapping" -> "PATCH";
            default -> "REQUEST";
        };
    }
}
