package com.manju.repolens.controller;

import com.manju.repolens.dto.ScanRepositoryRequest;
import com.manju.repolens.dto.ScanRepositoryResponse;
import com.manju.repolens.dto.ScanStatusResponse;
import com.manju.repolens.model.RepoScan;
import com.manju.repolens.service.graph.GraphQueryService;
import com.manju.repolens.service.scan.RepoScannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repos")
@Tag(name = "Repository Scanning", description = "APIs for scanning repositories")
public class RepoScanController {
    
    private final RepoScannerService scannerService;
    private final GraphQueryService graphQueryService;
    
    public RepoScanController(RepoScannerService scannerService, GraphQueryService graphQueryService) {
        this.scannerService = scannerService;
        this.graphQueryService = graphQueryService;
    }
    
    @PostMapping("/scan")
    @Operation(summary = "Scan a repository", description = "Initiates scanning of a GitHub or local repository")
    public ResponseEntity<ScanRepositoryResponse> scanRepository(@Valid @RequestBody ScanRepositoryRequest request) {
        RepoScan scan = scannerService.initiateScan(request);
        
        ScanRepositoryResponse response = new ScanRepositoryResponse(
            scan.getId(),
            scan.getStatus(),
            scan.getRepoName(),
            scan.getBranch(),
            scan.getTotalFilesScanned(),
            scan.getTotalNodes(),
            scan.getTotalEdges(),
            "Repository scan initiated"
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/scans/{scanId}")
    @Operation(summary = "Get scan status", description = "Retrieves the status of a repository scan")
    public ResponseEntity<ScanStatusResponse> getScanStatus(@PathVariable Long scanId) {
        ScanStatusResponse response = graphQueryService.getScanStatus(scanId);
        return ResponseEntity.ok(response);
    }
}
