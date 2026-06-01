# Maintenance Management System (MMS) — Backend

A Spring Boot REST API backend for managing maintenance requests, access tokens, and branch operations across companies. Built with Java, Spring Boot, and PostgreSQL.

---

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **PostgreSQL**
- **Spring Security + JWT** (for login and role protection)
- **Spring Data JPA** (for database access)
- **Lombok** (reduces boilerplate code)
- **Maven** (dependency and build management)

---

## Roles in the System

There are three user roles, each with different access:

| Role | What they can do |
|---|---|
| `ADMIN` | Full access — manages users, companies, branches, and reports |
| `BRANCH_MANAGER` | Manages their branch — verifies tokens, reviews reports, creates technicians |
| `TECHNICIAN` | Submits maintenance requests, views tokens, submits completion reports |

---

## Folder Structure

```
src/main/java/com/example/mms/
│
├── MmsApplication.java         ← Entry point of the app (@EnableScheduling is here)
│
├── model/                      ← Database tables (JPA Entities)
│   ├── User.java
│   ├── Company.java
│   ├── Branch.java
│   ├── ServiceType.java
│   ├── CompanyService.java
│   ├── TechnicianSkill.java
│   ├── MaintenanceRequest.java
│   ├── AccessToken.java
│   ├── TokenVerification.java
│   ├── CompletionReport.java
│   └── ActivityLog.java
│
├── enums/                      ← All enums live here (separate from model)
│   ├── Role.java               → ADMIN, BRANCH_MANAGER, TECHNICIAN
│   ├── Priority.java           → LOW, MEDIUM, HIGH, URGENT
│   ├── RequestStatus.java      → PENDING, APPROVED, REJECTED, IN_PROGRESS, COMPLETED
│   ├── TokenStatus.java        → ACTIVE, EXPIRED, REVOKED
│   ├── Decision.java           → GRANTED, DENIED
│   └── ApprovalStatus.java     → PENDING, APPROVED, REJECTED
│
├── dto/                        ← Data Transfer Objects (request/response shapes)
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── CreateUserRequest.java
│   ├── CreateCompanyRequest.java
│   ├── CreateBranchRequest.java
│   ├── MaintenanceRequestDTO.java
│   ├── UpdateStatusRequest.java
│   ├── TokenVerifyRequest.java
│   ├── CompletionReportRequest.java
│   ├── ReviewReportRequest.java
│   └── DashboardStats.java
│
├── repository/                 ← Database query interfaces (Spring Data JPA)
│   ├── UserRepository.java
│   ├── CompanyRepository.java
│   ├── BranchRepository.java
│   ├── ServiceTypeRepository.java
│   ├── CompanyServiceRepository.java
│   ├── TechnicianSkillRepository.java
│   ├── MaintenanceRequestRepository.java
│   ├── AccessTokenRepository.java
│   ├── TokenVerificationRepository.java
│   ├── CompletionReportRepository.java
│   └── ActivityLogRepository.java
│
├── service/                    ← Business logic (what the app actually does)
│   ├── AuthService.java        → Handles login for all roles
│   ├── UserService.java        → Create, deactivate users and technician skills
│   ├── CompanyService.java     → Create, update, deactivate companies + services
│   ├── BranchService.java      → Create, update, deactivate branches
│   ├── MaintenanceRequestService.java  → Submit requests, approve/reject, auto-generate tokens
│   ├── AccessTokenService.java → View tokens, verify tokens, revoke, expire
│   ├── CompletionReportService.java    → Submit and review completion reports
│   ├── DashboardService.java   → Dashboard summary stats per role
│   └── ActivityLogService.java → Log and retrieve activity for all users
│
├── controller/                 ← REST API endpoints (what the frontend calls)
│   ├── AuthController.java     → POST /api/auth/login
│   ├── DashboardController.java → GET /api/dashboard/admin | /manager/{branchId}
│   ├── UserController.java     → /api/users/**
│   ├── CompanyController.java  → /api/companies/**
│   ├── BranchController.java   → /api/branches/**
│   ├── MaintenanceRequestController.java → /api/requests/**
│   ├── AccessTokenController.java        → /api/tokens/**
│   ├── CompletionReportController.java   → /api/completion-reports/**
│   └── ActivityLogController.java        → /api/activity-logs/**
│
├── security/                   ← JWT token creation and validation
│   ├── JwtUtil.java            → Generates and reads JWT tokens
│   └── JwtFilter.java          → Checks the token on every request
│
├── config/                     ← App-wide configuration
│   └── SecurityConfig.java     → Defines which routes each role can access
│
└── scheduler/                  ← Background jobs
    └── TokenExpiryScheduler.java → Runs every 60s, marks expired tokens automatically
```

---

## What Each Layer Does (Simple Explanation)

**`model/`** — These are your database tables. Each file represents one table. JPA reads these and creates the actual tables in PostgreSQL automatically.

**`enums/`** — Fixed option lists used across the project. For example `Priority` holds the options LOW, MEDIUM, HIGH, URGENT. Kept in their own folder to stay organized.

**`dto/`** — These define the shape of data coming IN (requests from frontend) and going OUT (responses). They protect your database models from being exposed directly.

**`repository/`** — These are interfaces that talk to the database. You write method names like `findByBranchId()` and Spring figures out the SQL query automatically.

**`service/`** — This is where all the business logic lives. Services use repositories to get data, apply rules, and log activity. Controllers call services — never the repository directly.

**`controller/`** — These are your API endpoints. They receive HTTP requests, pass data to the service, and return the response. Each method maps to a URL.

**`security/`** — Handles JWT. When a user logs in they get a token. On every request after that, `JwtFilter` checks the token to know who is making the request.

**`config/`** — `SecurityConfig` sets the rules: which endpoints are public, which are Admin-only, which are for Branch Managers, etc.

**`scheduler/`** — A background job that runs automatically every 60 seconds to flip any `ACTIVE` tokens to `EXPIRED` if their expiry time has passed.

---

## How to Run the Project

### 1. Create the database in PostgreSQL
```sql
CREATE DATABASE mms_db;
```

### 2. Update `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mms_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the app
```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080` and creates all tables automatically.

---

## Accessing the API Documentation (Swagger UI)

Once the app is running, open your browser and go to:

http://localhost:8080/swagger-ui/index.html

You will see an interactive list of all API endpoints grouped by controller. You can expand each endpoint, see the required request body or parameters, and send test requests directly from the browser.

### How to authenticate in Swagger

Most endpoints require a JWT token. To test protected routes:

1. Use the **POST /api/auth/login** endpoint to log in with your username and password
2. Copy the token from the response
3. Click the **Authorize** button at the top right of the Swagger page
4. In the field, enter:

Bearer <paste_your_token_here>

5. Click **Authorize**, then **Close**

All subsequent requests you make from Swagger will now include your token automatically.

### Swagger is only available in development

The Swagger UI is enabled by default when running locally. Do not expose it in a production environment without proper access controls.

---

## API Quick Reference

| Method | Endpoint | Who |
|---|---|---|
| POST | `/api/auth/login` | Everyone |
| GET | `/api/dashboard/admin` | Admin |
| GET | `/api/dashboard/manager/{branchId}` | Branch Manager |
| POST | `/api/users/admin/create` | Admin |
| POST | `/api/users/manager/create` | Branch Manager |
| POST | `/api/companies` | Admin |
| POST | `/api/branches` | Admin |
| POST | `/api/requests` | Technician |
| PATCH | `/api/requests/{id}/status` | Branch Manager |
| GET | `/api/tokens/technician/{techId}` | Technician |
| POST | `/api/tokens/verify` | Branch Manager |
| POST | `/api/completion-reports` | Technician |
| PATCH | `/api/completion-reports/{id}/review` | Branch Manager |
| GET | `/api/activity-logs/user/{userId}` | Any logged-in user |

---

## Important Notes

- All requests (except `/api/auth/login`) require a **JWT token** in the header:
  ```
  Authorization: Bearer <your_token>
  ```
- Never use `@Enumerated(EnumType.ORDINAL)` — always use `EnumType.STRING`
- Passwords are stored as **BCrypt hashes** — never plain text
- When a maintenance request is **approved**, an access token is **auto-generated**
- When a completion report is **approved**, the maintenance request is automatically marked **COMPLETED**
