package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReadmeExtractor {
    
    private static final Pattern HEADING_PATTERN = Pattern.compile("^#+\\s+(.+)$", Pattern.MULTILINE);
    
    public List<CodeNode> extractSections(File readmeFile, RepoScan scan) {
        List<CodeNode> sections = new ArrayList<>();
        
        try {
            String content = Files.readString(readmeFile.toPath());
            Matcher matcher = HEADING_PATTERN.matcher(content);
            
            int sectionIndex = 0;
            while (matcher.find()) {
                String heading = matcher.group(1).trim();
                String nodeKey = "readme:section:" + sectionIndex++;
                
                CodeNode section = new CodeNode(scan, nodeKey, NodeType.README_SECTION, heading);
                section.setQualifiedName("README:" + heading);
                section.setFilePath("README.md");
                
                sections.add(section);
            }
            
        } catch (Exception e) {
            System.err.println("Failed to parse README: " + e.getMessage());
        }
        
        return sections;
    }
}
