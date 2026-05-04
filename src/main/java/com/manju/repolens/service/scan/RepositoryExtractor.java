package com.manju.repolens.service.scan;

import com.manju.repolens.service.scan.JavaSourceParserService.ParsedJavaFile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RepositoryExtractor {
    
    private static final Pattern JPA_REPOSITORY_PATTERN = 
        Pattern.compile("JpaRepository<\\s*([^,]+)\\s*,");
    
    public Optional<String> extractEntityType(ParsedJavaFile parsed) {
        if (!parsed.isInterface) {
            return Optional.empty();
        }
        
        for (String impl : parsed.implementsInterfaces) {
            Matcher matcher = JPA_REPOSITORY_PATTERN.matcher(impl);
            if (matcher.find()) {
                return Optional.of(matcher.group(1).trim());
            }
        }
        
        return Optional.empty();
    }
}
