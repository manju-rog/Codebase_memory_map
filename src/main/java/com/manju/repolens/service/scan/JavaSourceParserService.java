package com.manju.repolens.service.scan;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class JavaSourceParserService {
    
    public static class ParsedJavaFile {
        public String packageName;
        public String className;
        public String qualifiedName;
        public boolean isInterface;
        public boolean isClass;
        public List<String> annotations = new ArrayList<>();
        public List<ParsedMethod> methods = new ArrayList<>();
        public List<String> fieldTypes = new ArrayList<>();
        public List<String> constructorParamTypes = new ArrayList<>();
        public String extendsClass;
        public List<String> implementsInterfaces = new ArrayList<>();
        public int lineStart;
        public int lineEnd;
    }
    
    public static class ParsedMethod {
        public String name;
        public List<String> annotations = new ArrayList<>();
        public int lineStart;
        public int lineEnd;
        public boolean isPublic;
    }
    
    public Optional<ParsedJavaFile> parseJavaFile(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            CompilationUnit cu = StaticJavaParser.parse(in);
            
            ParsedJavaFile result = new ParsedJavaFile();
            
            cu.getPackageDeclaration().ifPresent(pkg -> 
                result.packageName = pkg.getNameAsString()
            );
            
            Optional<ClassOrInterfaceDeclaration> primaryType = cu.findFirst(ClassOrInterfaceDeclaration.class);
            
            if (primaryType.isEmpty()) {
                return Optional.empty();
            }
            
            ClassOrInterfaceDeclaration typeDecl = primaryType.get();
            result.className = typeDecl.getNameAsString();
            result.isInterface = typeDecl.isInterface();
            result.isClass = !typeDecl.isInterface();
            
            if (result.packageName != null) {
                result.qualifiedName = result.packageName + "." + result.className;
            } else {
                result.qualifiedName = result.className;
            }
            
            typeDecl.getAnnotations().forEach(ann -> 
                result.annotations.add(ann.getNameAsString())
            );
            
            typeDecl.getExtendedTypes().forEach(ext -> 
                result.extendsClass = ext.getNameAsString()
            );
            
            typeDecl.getImplementedTypes().forEach(impl -> 
                result.implementsInterfaces.add(impl.getNameAsString())
            );
            
            typeDecl.getFields().forEach(field -> {
                String fieldType = field.getElementType().asString();
                if (!result.fieldTypes.contains(fieldType)) {
                    result.fieldTypes.add(fieldType);
                }
            });
            
            typeDecl.getConstructors().forEach(constructor -> {
                constructor.getParameters().forEach(param -> {
                    String paramType = param.getType().asString();
                    if (!result.constructorParamTypes.contains(paramType)) {
                        result.constructorParamTypes.add(paramType);
                    }
                });
            });
            
            typeDecl.getMethods().forEach(method -> {
                ParsedMethod pm = new ParsedMethod();
                pm.name = method.getNameAsString();
                pm.isPublic = method.isPublic();
                method.getAnnotations().forEach(ann -> 
                    pm.annotations.add(ann.getNameAsString())
                );
                method.getBegin().ifPresent(pos -> pm.lineStart = pos.line);
                method.getEnd().ifPresent(pos -> pm.lineEnd = pos.line);
                result.methods.add(pm);
            });
            
            typeDecl.getBegin().ifPresent(pos -> result.lineStart = pos.line);
            typeDecl.getEnd().ifPresent(pos -> result.lineEnd = pos.line);
            
            return Optional.of(result);
            
        } catch (Exception e) {
            System.err.println("Failed to parse " + file.getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }
}
