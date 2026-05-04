package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import com.manju.repolens.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class SpringComponentExtractor {
    
    public Optional<CodeNode> extractComponent(ParsedJavaFile parsed, File file, File baseDir, RepoScan scan) {
        NodeType nodeType = determineNodeType(parsed);
        
        if (nodeType == null) {
            return Optional.empty();
        }
        
        String nodeKey = "class:" + parsed.qualifiedName;
        CodeNode node = new CodeNode(scan, nodeKey, nodeType, parsed.className);
        node.setQualifiedName(parsed.qualifiedName);
        node.setFilePath(getRelativePath(baseDir, file));
        node.setLineStart(parsed.lineStart);
        node.setLineEnd(parsed.lineEnd);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("annotations", parsed.annotations);
        metadata.put("isInterface", parsed.isInterface);
        if (parsed.extendsClass != null) {
            metadata.put("extends", parsed.extendsClass);
        }
        if (!parsed.implementsInterfaces.isEmpty()) {
            metadata.put("implements", parsed.implementsInterfaces);
        }
        node.setMetadataJson(JsonUtil.toJson(metadata));
        
        return Optional.of(node);
    }
    
    private NodeType determineNodeType(ParsedJavaFile parsed) {
        List<String> annotations = parsed.annotations;
        
        if (annotations.contains("RestController") || annotations.contains("Controller")) {
            return NodeType.CONTROLLER;
        }
        if (annotations.contains("Service")) {
            return NodeType.SERVICE;
        }
        if (annotations.contains("Repository")) {
            return NodeType.REPOSITORY_INTERFACE;
        }
        if (annotations.contains("Entity")) {
            return NodeType.ENTITY;
        }
        if (annotations.contains("Configuration")) {
            return NodeType.CONFIG;
        }
        if (annotations.contains("Component")) {
            return NodeType.COMPONENT;
        }
        
        // Check if it's a repository interface
        if (parsed.isInterface && parsed.implementsInterfaces.stream()
                .anyMatch(i -> i.contains("Repository") || i.contains("JpaRepository"))) {
            return NodeType.REPOSITORY_INTERFACE;
        }
        
        // Check if it's a DTO
        String className = parsed.className;
        if (className.endsWith("DTO") || className.endsWith("Dto") || 
            className.endsWith("Request") || className.endsWith("Response")) {
            return NodeType.DTO;
        }
        
        return NodeType.CLASS;
    }
    
    private String getRelativePath(File baseDir, File file) {
        try {
            return baseDir.toPath().relativize(file.toPath()).toString().replace("\\", "/");
        } catch (Exception e) {
            return file.getAbsolutePath();
        }
    }
}
