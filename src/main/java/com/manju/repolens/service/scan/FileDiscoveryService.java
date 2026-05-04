package com.manju.repolens.service.scan;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FileDiscoveryService {
    
    private static final Set<String> EXCLUDED_DIRS = Set.of(
        ".git", "target", "build", "out", "node_modules", ".idea", ".vscode", 
        "bin", "generated", ".gradle", "dist"
    );
    
    private static final Set<String> INCLUDED_EXTENSIONS = Set.of(
        ".java", ".xml", ".yml", ".yaml", ".properties", ".md"
    );
    
    private static final long MAX_FILE_SIZE_BYTES = 500 * 1024; // 500 KB
    
    public List<File> discoverFiles(File rootDir) {
        List<File> files = new ArrayList<>();
        discoverFilesRecursive(rootDir, files);
        return files;
    }
    
    private void discoverFilesRecursive(File dir, List<File> files) {
        if (!dir.isDirectory()) {
            return;
        }
        
        String dirName = dir.getName();
        if (EXCLUDED_DIRS.contains(dirName)) {
            return;
        }
        
        File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        
        for (File child : children) {
            if (child.isDirectory()) {
                discoverFilesRecursive(child, files);
            } else if (child.isFile() && shouldIncludeFile(child)) {
                files.add(child);
            }
        }
    }
    
    private boolean shouldIncludeFile(File file) {
        String name = file.getName().toLowerCase();
        
        boolean hasValidExtension = INCLUDED_EXTENSIONS.stream()
            .anyMatch(name::endsWith);
        
        if (!hasValidExtension) {
            return false;
        }
        
        return file.length() <= MAX_FILE_SIZE_BYTES;
    }
}
