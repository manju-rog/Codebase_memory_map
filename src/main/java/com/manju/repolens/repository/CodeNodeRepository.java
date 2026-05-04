package com.manju.repolens.repository;

import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.NodeType;
import com.manju.repolens.model.RepoScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeNodeRepository extends JpaRepository<CodeNode, Long> {
    
    List<CodeNode> findByScan(RepoScan scan);
    
    List<CodeNode> findByScanAndType(RepoScan scan, NodeType type);
    
    Optional<CodeNode> findByScanAndNodeKey(RepoScan scan, String nodeKey);
    
    @Query("SELECT n FROM CodeNode n WHERE n.scan = :scan AND " +
           "(LOWER(n.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(n.qualifiedName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(n.filePath) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<CodeNode> searchNodes(@Param("scan") RepoScan scan, @Param("query") String query);
}
