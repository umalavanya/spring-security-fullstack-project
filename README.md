# spring-security-fullstack-project comprehensive guide - by Uma


Project Overview
This is a full-stack web application demonstrating Spring Security integration with a React frontend. The application features user authentication, role-based authorization, and a MySQL database for user management.

Technology Stack
Backend
Spring Boot 3.2.x - Main framework
Spring Security - Authentication & Authorization
Spring Data JPA - Database operations
MySQL - Database
Maven - Dependency management

Frontend
React 18 - UI framework
Vite - Build tool & dev server
Axios - HTTP client
CSS3 - Styling

Project Structure
text
security-demo/                 (Spring Boot Backend)
├── src/
│   └── main/
│       └── java/
│           └── com/example/securitydemo/
│               ├── config/           (Security & CORS config)
│               ├── controller/       (REST endpoints)
│               ├── model/            (Entities & DTOs)
│               ├── repository/       (Data access layer)
│               ├── service/          (Business logic)
│               └── SecurityDemoApplication.java
│       └── resources/
│           └── application.properties
└── pom.xml

security-frontend/             (React Frontend)
├── src/
│   ├── App.jsx               (Main component)
│   ├── App.css               (Styles)
│   └── main.jsx              (Entry point)
├── vite.config.js            (Vite configuration)
└── package.json

***************Complete API Endpoints Reference******************
Authentication Endpoints
Method	Endpoint	Authentication	Description	Request Body	Response
POST	/api/auth/register	None	Register new user	{"username":"user","password":"pass","email":"email@example.com"}	{"message":"User registered successfully","username":"user"}
POST	/api/auth/login	Basic Auth	User login	None	{"message":"Login successful"}
GET	/api/auth/test	None	Test auth endpoint	None	"Auth endpoint is working!"
GET	/api/auth/users	None	Get all users (debug)	None	List<User>
Test Endpoints
Method	Endpoint	Authentication	Roles	Description	Response
GET	/api/test/public	None	Any	Public access test	{"message":"This is a public endpoint"}
GET	/api/test/secured	Basic Auth	USER, ADMIN	Authenticated access test	{"message":"This is a secured endpoint"}
GET	/api/test/admin	Basic Auth	ADMIN only	Admin-only access test	{"message":"This is an admin-only endpoint"}
Database Schema
Users Table
sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL
);
Default Users (Auto-created)
Regular User: testuser / password123 (ROLE_USER)

Admin User: admin / admin123 (ROLE_ADMIN)

Step-by-Step Implementation Guide
Phase 1: Backend Setup
Step 1: Create Spring Boot Project
Use Spring Initializr with: Web, Security, JPA, MySQL, Lombok

Configure application.properties for MySQL connection

Step 2: Create User Entity & Repository
Implement User entity with UserDetails

Create UserRepository with custom query methods

Step 3: Configure Spring Security
Create SecurityConfig with CORS, password encoder, and security rules

Implement UserService with UserDetailsService

Step 4: Create REST Controllers
AuthController for registration and authentication

TestController for testing different access levels

Phase 2: Frontend Setup
Step 5: Create React Application
Use Vite: npm create vite@latest security-frontend -- --template react

Install dependencies: npm install axios

Step 6: Build React Components
Create login/register forms

Implement authentication state management

Add endpoint testing functionality

Step 7: Configure API Communication
Set up Axios for HTTP requests

Handle CORS and authentication headers

Implement error handling

Security Configuration Details
Password Encoding
java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
CORS Configuration
Allows requests from http://localhost:5173 (React dev server)

Supports all HTTP methods (GET, POST, PUT, DELETE, OPTIONS)

Allows credentials and custom headers

Authorization Rules
Public: /api/auth/**, /api/test/public

Authenticated: /api/test/secured

Admin only: /api/test/admin

Testing the Application
Backend Testing (Using Postman)
1. Test Public Endpoint
http
GET http://localhost:8080/api/test/public
2. Test Registration
http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "newpass123",
  "email": "newuser@example.com"
}
3. Test Secured Endpoint (with Authentication)
http
GET http://localhost:8080/api/test/secured
Authorization: Basic dGVzdHVzZXI6cGFzc3dvcmQxMjM=
4. Test Admin Endpoint
http
GET http://localhost:8080/api/test/admin
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Frontend Testing
1. Start Both Servers
bash
# Terminal 1 - Backend
cd security-demo
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd security-frontend
npm run dev
2. Test Flow
Open http://localhost:5173

Login with test credentials

Test all endpoints via UI buttons

Register new users

Verify role-based access

Error Handling
Common Backend Errors
401 Unauthorized: Invalid credentials

403 Forbidden: Insufficient permissions

400 Bad Request: Validation errors (duplicate username/email)

Common Frontend Errors
CORS Errors: Check CORS configuration

Network Errors: Verify backend is running

Authentication Errors: Check credentials format

Security Features Implemented
Password Hashing: BCrypt password encoding

Role-Based Access Control: USER vs ADMIN roles

CORS Protection: Configured for specific origins

CSRF Protection: Disabled for API usage (stateless)

Session Management: Stateless (JWT-ready)

Input Validation: Backend validation for registration

Deployment Considerations
Backend
Configure production database

Update CORS for production domain

Set proper logging levels

Configure SSL/TLS

Frontend
Build for production: npm run build

Serve via Nginx/Apache

Update API base URL for production

Monitoring & Debugging
Backend Logs
Check Spring Boot console for startup errors

Monitor Hibernate SQL logs

Watch for authentication events

Frontend Debugging
Use browser Developer Tools

Check Network tab for API calls

Monitor Console for JavaScript errors

Extending the Application
Potential Enhancements
JWT Token Authentication: Replace Basic Auth

Email Verification: For user registration

Password Reset: Forgot password functionality

User Profile Management: Edit user details

Refresh Tokens: For better security

API Documentation: Swagger/OpenAPI

Logging: Comprehensive audit logs

Rate Limiting: Prevent abuse

Database Enhancements
sql
-- Add created_at and updated_at timestamps
ALTER TABLE users ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE users ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Add account status fields
ALTER TABLE users ADD enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE users ADD account_non_locked BOOLEAN DEFAULT TRUE;
Troubleshooting Guide
Backend Issues
Database Connection: Check MySQL credentials and database existence

CORS Errors: Verify CORS configuration matches frontend URL

Authentication Failures: Check user roles and password encoding

Frontend Issues
Network Errors: Verify backend is running on port 8080

CORS Issues: Check browser console for CORS policy violations

State Management: Clear localStorage if authentication state gets stuck

Common Fixes
Restart both servers after configuration changes
Clear browser cache and localStorage
Check console logs for detailed error messages
Verify database tables are created properly

