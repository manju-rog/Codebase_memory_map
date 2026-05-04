package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.model.SourceType;
import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EntityExtractorTest {
    
    private final EntityExtractor extractor = new EntityExtractor();
    
    @Test
    void shouldExtractDatabaseTableFromEntity() {
        RepoScan scan = new RepoScan(SourceType.LOCAL, "test", null, null, "main");
        
        ParsedJavaFile parsed = new ParsedJavaFile();
        parsed.annotations.add("Entity");
        parsed.className = "User";
        parsed.qualifiedName = "com.example.User";
        
        Optional<CodeNode> tableNode = extractor.extractDatabaseTable(parsed, scan);
        
        assertTrue(tableNode.isPresent());
        assertEquals(NodeType.DATABASE_TABLE, tableNode.get().getType());
        assertEquals("user", tableNode.get().getName());
    }
    
    @Test
    void shouldNotExtractTableFromNonEntity() {
        RepoScan scan = new RepoScan(SourceType.LOCAL, "test", null, null, "main");
        
        ParsedJavaFile parsed = new ParsedJavaFile();
        parsed.className = "UserService";
        parsed.qualifiedName = "com.example.UserService";
        
        Optional<CodeNode> tableNode = extractor.extractDatabaseTable(parsed, scan);
        
        assertFalse(tableNode.isPresent());
    }
}
