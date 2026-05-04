package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.model.SourceType;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedMethod;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EndpointExtractorTest {
    
    private final EndpointExtractor extractor = new EndpointExtractor();
    
    @Test
    void shouldExtractPostMappingEndpoint() {
        RepoScan scan = new RepoScan(SourceType.LOCAL, "test", null, null, "main");
        
        ParsedJavaFile parsed = new ParsedJavaFile();
        parsed.annotations.add("RestController");
        parsed.className = "AuthController";
        parsed.qualifiedName = "com.example.AuthController";
        
        ParsedMethod method = new ParsedMethod();
        method.name = "login";
        method.annotations.add("PostMapping");
        parsed.methods.add(method);
        
        CodeNode controllerNode = new CodeNode(scan, "class:com.example.AuthController", 
            NodeType.CONTROLLER, "AuthController");
        
        List<CodeNode> endpoints = extractor.extractEndpoints(parsed, controllerNode, scan);
        
        assertEquals(1, endpoints.size());
        CodeNode endpoint = endpoints.get(0);
        assertEquals(NodeType.ENDPOINT, endpoint.getType());
        assertTrue(endpoint.getName().contains("POST"));
    }
}
