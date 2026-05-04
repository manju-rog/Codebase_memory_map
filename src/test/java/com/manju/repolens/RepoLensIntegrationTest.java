package com.manju.repolens;

import com.manju.repolens.dto.AskQuestionRequest;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.dto.ScanRepositoryRequest;
import com.manju.repolens.dto.ScanRepositoryResponse;
import com.manju.repolens.model.SourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepoLensIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testCompleteScanAndQuestionFlow() throws InterruptedException {
        // Step 1: Scan the sample repository
        ScanRepositoryRequest scanRequest = new ScanRepositoryRequest(
            SourceType.LOCAL,
            null,
            "./sample-repos/mini-bank-api",
            "main"
        );
        
        ResponseEntity<ScanRepositoryResponse> scanResponse = restTemplate.postForEntity(
            "/api/repos/scan",
            scanRequest,
            ScanRepositoryResponse.class
        );
        
        assertEquals(HttpStatus.OK, scanResponse.getStatusCode());
        assertNotNull(scanResponse.getBody());
        Long scanId = scanResponse.getBody().scanId();
        assertNotNull(scanId);
        
        // Wait for scan to complete
        Thread.sleep(5000);
        
        // Step 2: Ask a question
        AskQuestionRequest questionRequest = new AskQuestionRequest(
            "Where is login implemented?"
        );
        
        ResponseEntity<AskQuestionResponse> answerResponse = restTemplate.postForEntity(
            "/api/repos/scans/" + scanId + "/ask",
            questionRequest,
            AskQuestionResponse.class
        );
        
        assertEquals(HttpStatus.OK, answerResponse.getStatusCode());
        assertNotNull(answerResponse.getBody());
        
        AskQuestionResponse answer = answerResponse.getBody();
        assertNotNull(answer.answer());
        assertTrue(answer.answer().toLowerCase().contains("auth") || 
                   answer.answer().toLowerCase().contains("login"));
        assertTrue(answer.confidence() > 0);
        
        System.out.println("=== INTEGRATION TEST RESULTS ===");
        System.out.println("Scan ID: " + scanId);
        System.out.println("Answer: " + answer.answer());
        System.out.println("Confidence: " + (answer.confidence() * 100) + "%");
        System.out.println("Evidence count: " + answer.evidence().size());
        System.out.println("Affected files: " + answer.affectedFiles().size());
    }
}
