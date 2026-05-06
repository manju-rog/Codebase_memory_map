# 💡 Example Questions for RepoLens AI

## 🎯 Questions You Can Ask

RepoLens AI can answer natural language questions about your codebase. Here are examples organized by category.

---

## 📂 For Demo Endpoint (`/api/demo/ask`)

The demo analyzes the `test-program/` directory with these files:
- `LoginService.java`
- `UserRepository.java`
- `TokenService.java`
- `User.java`
- `LoginResponse.java`

### 🔐 Authentication & Login Questions

1. **"Where is the login logic implemented?"**
   - Shows: LoginService.login() method details

2. **"How does authentication work?"**
   - Shows: Complete authentication flow

3. **"What happens during login?"**
   - Shows: Step-by-step login process

4. **"How is the password verified?"**
   - Shows: Password verification logic

5. **"What classes are involved in login?"**
   - Shows: All classes used in authentication

### 🎫 Token Questions

6. **"How is the token generated?"**
   - Shows: TokenService.generateToken() method

7. **"What does TokenService do?"**
   - Shows: Token service responsibilities

8. **"Where is the JWT token created?"**
   - Shows: Token creation logic

9. **"How does token generation work?"**
   - Shows: Token generation process

### 👤 User Questions

10. **"What does UserRepository do?"**
    - Shows: User data access logic

11. **"How are users retrieved?"**
    - Shows: User lookup process

12. **"What is the User class?"**
    - Shows: User entity structure

13. **"How is user data accessed?"**
    - Shows: Repository pattern usage

### 🔄 Flow Questions

14. **"Show me the authentication flow"**
    - Shows: Complete flow from login to token

15. **"What is the login process?"**
    - Shows: Step-by-step login process

16. **"How do the classes work together?"**
    - Shows: Class relationships and interactions

17. **"What is the relationship between LoginService and UserRepository?"**
    - Shows: Dependency relationships

### 📊 Structure Questions

18. **"What methods does LoginService have?"**
    - Shows: All methods in LoginService

19. **"What are the dependencies of LoginService?"**
    - Shows: UserRepository and TokenService dependencies

20. **"What does LoginResponse contain?"**
    - Shows: Response object structure

---

## 🏦 For Mini-Bank-API Sample Repository

After scanning the `mini-bank-api` repository, you can ask:

### 🔐 Authentication Questions

21. **"Where is login implemented?"**
    - Shows: AuthController → AuthService → UserRepository

22. **"How does JWT authentication work?"**
    - Shows: JwtService and SecurityConfig

23. **"What endpoints require authentication?"**
    - Shows: Secured endpoints

24. **"How is the JWT token validated?"**
    - Shows: Token validation logic

### 💰 Account Questions

25. **"How do I get account balance?"**
    - Shows: AccountController → AccountService → AccountRepository

26. **"What APIs are available for accounts?"**
    - Shows: All account endpoints

27. **"How are accounts stored in the database?"**
    - Shows: Account entity and table mapping

28. **"What is the account entity structure?"**
    - Shows: Account class fields and relationships

### 💸 Transfer Questions

29. **"How does fund transfer work?"**
    - Shows: TransferController → TransferService → AccountRepository

30. **"What is the transfer flow?"**
    - Shows: Complete transfer process

31. **"How are transactions recorded?"**
    - Shows: Transaction entity and repository

32. **"What validations are done during transfer?"**
    - Shows: Transfer validation logic

### 🗄️ Database Questions

33. **"What database tables exist?"**
    - Shows: All entities and their table mappings

34. **"How is the User entity mapped to database?"**
    - Shows: User entity and users table

35. **"What is the relationship between Account and User?"**
    - Shows: Entity relationships

36. **"Which repositories access the database?"**
    - Shows: All repository classes

### 🌐 API Questions

37. **"What REST endpoints are available?"**
    - Shows: All controllers and their endpoints

38. **"Which APIs use UserRepository?"**
    - Shows: AuthController, AuthService

39. **"What does the /api/auth/login endpoint do?"**
    - Shows: Login endpoint implementation

40. **"Show me all POST endpoints"**
    - Shows: All POST request mappings

### ⚙️ Configuration Questions

41. **"Which config controls JWT?"**
    - Shows: SecurityConfig, JwtService, application.yml

42. **"How is security configured?"**
    - Shows: SecurityConfig class

43. **"What is in the application configuration?"**
    - Shows: application.yml settings

44. **"How is the database configured?"**
    - Shows: Database configuration in application.yml

### 🔍 Impact Analysis Questions

45. **"Which files are affected if I change user role logic?"**
    - Shows: User.java, UserRepository.java, AuthService.java, SecurityConfig.java

46. **"What would break if I modify the Account entity?"**
    - Shows: All classes depending on Account

47. **"Which classes depend on UserRepository?"**
    - Shows: AuthService and other dependent classes

48. **"What happens if I change the JWT secret?"**
    - Shows: Affected configuration and services

### 📦 Component Questions

49. **"What services exist in the application?"**
    - Shows: AuthService, AccountService, TransferService

50. **"What controllers are available?"**
    - Shows: AuthController, AccountController, TransferController

51. **"List all repositories"**
    - Shows: UserRepository, AccountRepository, TransactionRepository

52. **"What entities are defined?"**
    - Shows: User, Account, Transaction

---

## 🎨 Question Patterns That Work

### Pattern 1: "Where is X?"
- "Where is login implemented?"
- "Where is the JWT token generated?"
- "Where is user validation done?"

### Pattern 2: "How does X work?"
- "How does authentication work?"
- "How does fund transfer work?"
- "How does token validation work?"

### Pattern 3: "What is X?"
- "What is UserRepository?"
- "What is the login flow?"
- "What is SecurityConfig?"

### Pattern 4: "Show me X"
- "Show me the authentication flow"
- "Show me all endpoints"
- "Show me the database schema"

### Pattern 5: "Which/What X?"
- "Which classes use UserRepository?"
- "What endpoints require authentication?"
- "Which files would be affected if...?"

### Pattern 6: "How do I X?"
- "How do I authenticate a user?"
- "How do I transfer funds?"
- "How do I get account balance?"

### Pattern 7: "List X"
- "List all controllers"
- "List all services"
- "List all database tables"

---

## 🧪 Testing Different Questions

### Test in Terminal (Demo Endpoint)
```bash
# Question 1
curl "http://localhost:8080/api/demo/ask?question=Where+is+the+login+logic+implemented"

# Question 2
curl "http://localhost:8080/api/demo/ask?question=How+does+authentication+work"

# Question 3
curl "http://localhost:8080/api/demo/ask?question=How+is+the+token+generated"

# Question 4
curl "http://localhost:8080/api/demo/ask?question=What+does+UserRepository+do"
```

### Test in Browser UI
1. Open: http://localhost:8080
2. Type any question from the list above
3. Press Enter
4. See AI answer + visual graph

---

## 💡 Tips for Better Questions

### ✅ Good Questions (Specific)
- "Where is the login logic implemented?"
- "How does the TokenService generate tokens?"
- "What classes are involved in authentication?"

### ❌ Avoid Vague Questions
- "Tell me about the code"
- "What does this do?"
- "Explain everything"

### ✅ Use Domain Terms
- Use actual class names: "UserRepository", "LoginService"
- Use feature names: "authentication", "login", "token"
- Use technical terms: "JWT", "validation", "endpoint"

### ✅ Ask About Relationships
- "How do LoginService and UserRepository work together?"
- "What is the relationship between User and Account?"
- "Which classes depend on TokenService?"

### ✅ Ask About Flow
- "Show me the authentication flow"
- "What happens during login?"
- "How does fund transfer work?"

---

## 🎯 Questions by Skill Level

### 👶 Beginner Questions
1. "What does LoginService do?"
2. "Where is the login code?"
3. "What is UserRepository?"
4. "How do I authenticate?"

### 👨‍💻 Intermediate Questions
5. "How does authentication work?"
6. "What is the login flow?"
7. "Which classes are involved in login?"
8. "How is the token generated?"

### 🧙 Advanced Questions
9. "What would break if I change User entity?"
10. "Show me the complete authentication flow from controller to database"
11. "Which files are affected if I modify JWT configuration?"
12. "What is the relationship between all authentication components?"

---

## 📊 Questions by Category

### 🏗️ Architecture
- "What is the application structure?"
- "How are layers organized?"
- "What design patterns are used?"

### 🔐 Security
- "How is authentication implemented?"
- "Where is authorization checked?"
- "How are passwords handled?"

### 🗄️ Data
- "What database tables exist?"
- "How is data persisted?"
- "What are the entity relationships?"

### 🌐 API
- "What endpoints are available?"
- "How do I call the login API?"
- "What does the transfer endpoint do?"

### 🔄 Business Logic
- "How does fund transfer work?"
- "What validations are performed?"
- "How is balance calculated?"

---

## 🚀 Try These Now!

### Quick Test (5 Questions)
1. "Where is the login logic implemented?"
2. "How does authentication work?"
3. "What does UserRepository do?"
4. "How is the token generated?"
5. "What classes are involved in login?"

### Deep Dive (5 Questions)
1. "Show me the complete authentication flow"
2. "How do LoginService and UserRepository work together?"
3. "What happens step by step during login?"
4. "What is the relationship between all classes?"
5. "How does token generation work in detail?"

---

## 📝 Create Your Own Questions

### Template 1: Feature Location
"Where is [feature] implemented?"
- Where is login implemented?
- Where is token generation implemented?
- Where is user validation implemented?

### Template 2: How It Works
"How does [feature] work?"
- How does authentication work?
- How does token validation work?
- How does password verification work?

### Template 3: Component Purpose
"What does [component] do?"
- What does LoginService do?
- What does UserRepository do?
- What does TokenService do?

### Template 4: Relationships
"What is the relationship between [A] and [B]?"
- What is the relationship between LoginService and UserRepository?
- What is the relationship between User and Account?

### Template 5: Dependencies
"Which classes depend on [component]?"
- Which classes depend on UserRepository?
- Which classes depend on TokenService?

---

## 🎉 Have Fun Exploring!

The AI can answer almost any question about the codebase structure, flow, and relationships. Try different phrasings and see what works best!

**Remember**: The more specific your question, the better the answer!

---

**Last Updated**: May 5, 2026
**Questions**: 50+ examples
**Categories**: 10+ categories
