package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import com.manju.repolens.util.JsonUtil;
import com.manju.repolens.util.NameNormalizer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EntityExtractor {
    
    public Optional<CodeNode> extractDatabaseTable(ParsedJavaFile parsed, RepoScan scan) {
        if (!parsed.annotations.contains("Entity")) {
            return Optional.empty();
        }
        
        String tableName = extractTableName(parsed);
        String nodeKey = "table:" + tableName;
        
        CodeNode tableNode = new CodeNode(scan, nodeKey, NodeType.DATABASE_TABLE, tableName);
        tableNode.setQualifiedName(tableName);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("entityClass", parsed.qualifiedName);
        tableNode.setMetadataJson(JsonUtil.toJson(metadata));
        
        return Optional.of(tableNode);
    }
    
    private String extractTableName(ParsedJavaFile parsed) {
        // Simplified: In real implementation, would parse @Table annotation
        // For now, convert class name to snake_case
        return NameNormalizer.toSnakeCase(parsed.className);
    }
}
