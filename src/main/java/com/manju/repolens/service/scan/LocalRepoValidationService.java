package com.manju.repolens.service.scan;

import com.manju.repolens.exception.BadRequestException;
import com.manju.repolens.util.PathSanitizer;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LocalRepoValidationService {
    
    public File validateAndGetDirectory(String localPath) {
        if (localPath == null || localPath.isBlank()) {
            throw new BadRequestException("localPath is required when sourceType is LOCAL");
        }
        
        if (!PathSanitizer.isPathSafe(localPath)) {
            throw new BadRequestException("localPath must be within the current workspace directory");
        }
        
        File dir = new File(localPath);
        if (!dir.exists()) {
            throw new BadRequestException("localPath does not exist: " + localPath);
        }
        
        if (!dir.isDirectory()) {
            throw new BadRequestException("localPath is not a directory: " + localPath);
        }
        
        return dir;
    }
}
