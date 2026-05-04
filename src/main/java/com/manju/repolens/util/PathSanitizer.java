package com.manju.repolens.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathSanitizer {
    
    public static boolean isPathSafe(String localPath) {
        try {
            Path path = Paths.get(localPath).toAbsolutePath().normalize();
            Path currentDir = Paths.get(".").toAbsolutePath().normalize();
            return path.startsWith(currentDir);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String normalizePath(String path) {
        return path.replace("\\", "/");
    }
    
    public static String getRelativePath(File baseDir, File file) {
        try {
            return baseDir.toPath().relativize(file.toPath()).toString().replace("\\", "/");
        } catch (Exception e) {
            return file.getAbsolutePath();
        }
    }
}
