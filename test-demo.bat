@echo off
echo ========================================
echo RepoLens AI - Demo Test
echo ========================================
echo.

echo Testing Demo Endpoint...
echo Question: Where is the login logic implemented?
echo.

curl "http://localhost:8080/api/demo/ask?question=Where+is+the+login+logic+implemented" > demo-response.json

echo.
echo ========================================
echo Response saved to: demo-response.json
echo ========================================
echo.

echo Opening UI in browser...
start http://localhost:8080

echo.
echo ========================================
echo Demo Test Complete!
echo ========================================
echo.
echo Try these questions in the UI:
echo - Where is the login logic implemented?
echo - How does authentication work?
echo - What classes are involved in login?
echo.

pause
