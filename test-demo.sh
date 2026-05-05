#!/bin/bash

echo "========================================"
echo "RepoLens AI - Demo Test"
echo "========================================"
echo ""

echo "Testing Demo Endpoint..."
echo "Question: Where is the login logic implemented?"
echo ""

# Test the endpoint
curl "http://localhost:8080/api/demo/ask?question=Where+is+the+login+logic+implemented" \
  -o demo-response.json \
  -w "\n\nHTTP Status: %{http_code}\n"

echo ""
echo "========================================"
echo "Response saved to: demo-response.json"
echo "========================================"
echo ""

# Check if response file was created
if [ -f "demo-response.json" ]; then
    echo "✅ Demo test successful!"
    echo ""
    echo "View the response:"
    echo "  cat demo-response.json | jq '.answer'"
    echo ""
else
    echo "❌ Demo test failed!"
    echo "Make sure the application is running on http://localhost:8080"
    exit 1
fi

echo "Opening UI in browser..."
if command -v xdg-open &> /dev/null; then
    xdg-open http://localhost:8080
elif command -v open &> /dev/null; then
    open http://localhost:8080
else
    echo "Please open http://localhost:8080 in your browser"
fi

echo ""
echo "========================================"
echo "Demo Test Complete!"
echo "========================================"
echo ""
echo "Try these questions in the UI:"
echo "- Where is the login logic implemented?"
echo "- How does authentication work?"
echo "- What classes are involved in login?"
echo ""
