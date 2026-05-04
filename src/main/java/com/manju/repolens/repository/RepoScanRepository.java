package com.manju.repolens.repository;

import com.manju.repolens.model.RepoScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoScanRepository extends JpaRepository<RepoScan, Long> {
}
