package com.manju.repolens.repository;

import com.manju.repolens.model.CodeEdge;
import com.manju.repolens.model.CodeNode;
import com.manju.repolens.model.RepoScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeEdgeRepository extends JpaRepository<CodeEdge, Long> {
    
    List<CodeEdge> findByScan(RepoScan scan);
    
    List<CodeEdge> findBySourceNode(CodeNode sourceNode);
    
    List<CodeEdge> findByTargetNode(CodeNode targetNode);
    
    List<CodeEdge> findBySourceNodeOrTargetNode(CodeNode sourceNode, CodeNode targetNode);
}
