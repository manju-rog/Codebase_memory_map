package com.manju.repolens.service.scan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDiscoveryServiceTest {
    
    private final FileDiscoveryService service = new FileDiscoveryService();
    
    @Test
    void shouldDiscoverJavaFiles(@TempDir Path tempDir) throws IOException {
        // Create test files
        Files.createFile(tempDir.resolve("Test.java"));
        Files.createFile(tempDir.resolve("pom.xml"));
        Files.createFile(tempDir.resolve("README.md"));
        
        List<File> files = service.discoverFiles(tempDir.toFile());
        
        assertEquals(3, files.size());
    }
    
    @Test
    void shouldExcludeTargetDirectory(@TempDir Path tempDir) throws IOException {
        // Create target directory
        Path targetDir = tempDir.resolve("target");
        Files.createDirectory(targetDir);
        Files.createFile(targetDir.resolve("Test.class"));
        
        // Create source file
        Files.createFile(tempDir.resolve("Source.java"));
        
        List<File> files = service.discoverFiles(tempDir.toFile());
        
        assertEquals(1, files.size());
        assertEquals("Source.java", files.get(0).getName());
    }
    
    @Test
    void shouldExcludeGitDirectory(@TempDir Path tempDir) throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectory(gitDir);
        Files.createFile(gitDir.resolve("config"));
        
        Files.createFile(tempDir.resolve("Source.java"));
        
        List<File> files = service.discoverFiles(tempDir.toFile());
        
        assertEquals(1, files.size());
    }
}
