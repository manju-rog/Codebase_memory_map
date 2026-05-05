# Simple OCI GenAI Test Script
Write-Host "=== Testing OCI GenAI Integration ===" -ForegroundColor Cyan
Write-Host ""

# Test 1: Simple prompt
Write-Host "Test 1: Testing with simple prompt..." -ForegroundColor Yellow
$body1 = @{
    prompt = "What is Spring Boot? Answer in one sentence."
} | ConvertTo-Json

try {
    $response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/test/ai" -Method POST -Body $body1 -ContentType "application/json"
    Write-Host "✓ Status: $($response1.status)" -ForegroundColor Green
    Write-Host "✓ Answer: $($response1.answer)" -ForegroundColor Green
    Write-Host "✓ Confidence: $($response1.confidence)" -ForegroundColor Green
} catch {
    Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Response: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Test Complete ===" -ForegroundColor Cyan
