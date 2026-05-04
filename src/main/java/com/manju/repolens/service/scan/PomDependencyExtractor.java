package com.manju.repolens.service.scan;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PomDependencyExtractor {
    
    public List<CodeNode> extractDependencies(File pomFile, RepoScan scan) {
        List<CodeNode> dependencies = new ArrayList<>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pomFile);
            
            NodeList dependencyNodes = doc.getElementsByTagName("dependency");
            
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Element depElement = (Element) dependencyNodes.item(i);
                
                String groupId = getTextContent(depElement, "groupId");
                String artifactId = getTextContent(depElement, "artifactId");
                String version = getTextContent(depElement, "version");
                
                if (artifactId != null && !artifactId.isBlank()) {
                    String nodeKey = "dependency:" + artifactId;
                    CodeNode depNode = new CodeNode(scan, nodeKey, NodeType.DEPENDENCY, artifactId);
                    depNode.setQualifiedName(groupId + ":" + artifactId);
                    
                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("groupId", groupId);
                    metadata.put("version", version);
                    depNode.setMetadataJson(JsonUtil.toJson(metadata));
                    
                    dependencies.add(depNode);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Failed to parse pom.xml: " + e.getMessage());
        }
        
        return dependencies;
    }
    
    private String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }
}
