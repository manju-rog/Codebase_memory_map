package com.manju.repolens.util;

public class NameNormalizer {
    
    public static String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    public static String extractRepoName(String repoUrl) {
        if (repoUrl == null || repoUrl.isEmpty()) {
            return "unknown";
        }
        
        String name = repoUrl;
        if (name.endsWith(".git")) {
            name = name.substring(0, name.length() - 4);
        }
        
        int lastSlash = name.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < name.length() - 1) {
            name = name.substring(lastSlash + 1);
        }
        
        return name;
    }
    
    public static String combinePaths(String basePath, String methodPath) {
        if (basePath == null) basePath = "";
        if (methodPath == null) methodPath = "";
        
        basePath = basePath.trim();
        methodPath = methodPath.trim();
        
        if (!basePath.startsWith("/")) basePath = "/" + basePath;
        if (basePath.endsWith("/")) basePath = basePath.substring(0, basePath.length() - 1);
        
        if (!methodPath.startsWith("/")) methodPath = "/" + methodPath;
        
        return basePath + methodPath;
    }
}
