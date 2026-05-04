package com.manju.repolens.service.scan;

import com.manju.repolens.exception.ScanFailedException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class GitCloneService {
    
    @Value("${app.scanner.temp-dir}")
    private String tempDir;
    
    public File cloneRepository(String repoUrl, String branch) {
        try {
            Path tempPath = Files.createTempDirectory(Path.of(tempDir), "repolens-");
            File cloneDir = tempPath.toFile();
            
            String githubToken = System.getenv("GITHUB_TOKEN");
            
            Git git;
            if (githubToken != null && !githubToken.isBlank()) {
                git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(cloneDir)
                    .setBranch(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""))
                    .call();
            } else {
                git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(cloneDir)
                    .setBranch(branch)
                    .call();
            }
            
            git.close();
            return cloneDir;
            
        } catch (Exception e) {
            throw new ScanFailedException("Failed to clone repository: " + e.getMessage(), e);
        }
    }
    
    public void cleanupDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        
        try {
            Files.walk(dir.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            // Log but don't fail
            System.err.println("Failed to cleanup directory: " + e.getMessage());
        }
    }
}
