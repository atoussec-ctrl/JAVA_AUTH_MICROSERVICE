# 📬 Auth Service - Postman Collection Guide

## 📥 Installation

### Option 1: Import Collection & Environment
1. Open **Postman**
2. Click **Import** (top-left)
3. Select **Auth-Service-Collection.postman_collection.json**
4. Click **Import**
5. Then import **Auth-Service-Environment.postman_environment.json**
6. Select the "Auth Service - Local Environment" from the dropdown (top-right)

### Option 2: Direct Link
If you have this collection hosted, use the link to import directly.

---

## 🚀 Quick Start

### 1️⃣ **Health Check**
Verify the service is running:
```
GET http://localhost:8080/actuator/health
```

### 2️⃣ **Register a New User**
```
POST /api/v1/auth/register
Body:
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword@123",
  "passwordConfirmation": "SecurePassword@123"
}
Response: HTTP 201 CREATED
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john@example.com",
  "enabled": true,
  "emailVerified": false,
  "createdAt": "2026-06-22T..."
}
```
✅ Variables saved: `userId`, `userEmail`

### 3️⃣ **Login**
```
POST /api/v1/auth/login
Body:
{
  "email": "john@example.com",
  "password": "SecurePassword@123"
}
Response: HTTP 200 OK
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "refresh_token_value",
  "type": "Bearer",
  "expiresIn": 900
}
```
✅ Variables saved automatically: `accessToken`, `refreshToken`, `expiresIn`

### 4️⃣ **Use Protected Endpoints**
Any endpoint requiring authentication will automatically use the `accessToken` from the Bearer token.

---

## 📋 Available Endpoints

### 🔐 **Authentication** (No Bearer Token Required)

#### 📝 Register User
- **Method:** `POST`
- **URL:** `/api/v1/auth/register`
- **Headers:** `Content-Type: application/json`
- **Body:** 
  ```json
  {
    "name": "string (2-120 chars)",
    "email": "valid@email.com",
    "password": "string (min 8 chars)",
    "passwordConfirmation": "string (must match password)"
  }
  ```
- **Response:** `201 CREATED`
- **Auto-Variables:** `userId`, `userEmail`

#### 🔑 Login
- **Method:** `POST`
- **URL:** `/api/v1/auth/login`
- **Body:**
  ```json
  {
    "email": "valid@email.com",
    "password": "password123"
  }
  ```
- **Response:** `200 OK` with tokens
- **Auto-Variables:** `accessToken`, `refreshToken`, `expiresIn`

#### 🔄 Refresh Token
- **Method:** `POST`
- **URL:** `/api/v1/auth/refresh`
- **Body:**
  ```json
  {
    "refreshToken": "{{refreshToken}}"
  }
  ```
- **Response:** `200 OK` with new access token
- **Auto-Variables:** `accessToken`

#### 🚪 Logout
- **Method:** `POST`
- **URL:** `/api/v1/auth/logout`
- **Headers:** `Authorization: Bearer {{accessToken}}`
- **Body:**
  ```json
  {
    "refreshToken": "{{refreshToken}}"
  }
  ```
- **Response:** `200 OK`
- **Auto-Variables:** Clears `accessToken` and `refreshToken`

#### ✅ Validate Token
- **Method:** `POST`
- **URL:** `/api/v1/auth/validate`
- **Body:**
  ```json
  {
    "accessToken": "{{accessToken}}"
  }
  ```
- **Response:** `200 OK`
  ```json
  {
    "valid": true,
    "userId": "uuid",
    "email": "user@example.com",
    "roles": ["ROLE_USER"],
    "permissions": ["read", "write"],
    "issuedAt": "2026-06-22T...",
    "expiresAt": "2026-06-22T..."
  }
  ```

---

### 🔒 **Password Management** (No Bearer Token Required)

#### 📧 Forgot Password
- **Method:** `POST`
- **URL:** `/api/v1/auth/forgot-password`
- **Body:**
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response:** `200 OK`
  ```json
  {
    "message": "Password reset email sent"
  }
  ```
- **Note:** Reset token is sent to the email address (in production)

#### 🔐 Reset Password
- **Method:** `POST`
- **URL:** `/api/v1/auth/reset-password`
- **Body:**
  ```json
  {
    "resetToken": "token-from-email",
    "newPassword": "NewPassword@456",
    "passwordConfirmation": "NewPassword@456"
  }
  ```
- **Response:** `200 OK`
  ```json
  {
    "message": "Password reset successfully"
  }
  ```

---

### 📊 **Health & Status** (No Bearer Token Required)

#### 💚 Health Check
- **Method:** `GET`
- **URL:** `/actuator/health`
- **Response:** `200 OK`
  ```json
  {
    "status": "UP",
    "components": {
      "db": {
        "status": "UP"
      },
      "diskSpace": {
        "status": "UP"
      }
    }
  }
  ```

---

## 🔧 Environment Variables

| Variable | Purpose | Auto-Set? |
|----------|---------|-----------|
| `baseUrl` | API base URL (default: http://localhost:8080) | ❌ |
| `accessToken` | JWT access token for protected endpoints | ✅ |
| `refreshToken` | Token for refreshing access token | ✅ |
| `userId` | Current user's ID | ✅ |
| `userEmail` | Current user's email | ✅ |
| `tokenType` | Token type (default: Bearer) | ❌ |
| `expiresIn` | Token expiration time in seconds | ✅ |
| `resetToken` | Password reset token (manual) | ❌ |

---

## 📝 Test Workflows

### Workflow 1: Complete Registration & Login
1. **Register User** → Creates new account → Saves `userId`, `userEmail`
2. **Login** → Authenticates → Saves `accessToken`, `refreshToken`
3. **Validate Token** → Verifies JWT is valid
4. **Logout** → Invalidates tokens

### Workflow 2: Password Recovery
1. **Forgot Password** → Sends reset email with token
2. **Reset Password** → Sets new password (manually set `resetToken` variable)

### Workflow 3: Token Refresh
1. **Login** → Get initial tokens
2. **Refresh Token** → Get new access token before expiration
3. **Validate Token** → Verify new token works

---

## 🧪 Automated Tests

Each request includes **pre-request scripts** and **test scripts**:

- **Pre-request Scripts:** Set up dynamic data (timestamps, random emails)
- **Test Scripts:** Validate responses and save variables automatically

### Run Test Suite
1. Select entire collection
2. Click **Run** (top-right)
3. Click **Run Auth Service Collection**
4. Review results in console

---

## 🐛 Troubleshooting

### ❌ 401 Unauthorized
- **Cause:** Access token expired or invalid
- **Solution:** Run **Refresh Token** or **Login** again

### ❌ 400 Bad Request
- **Cause:** Invalid request body (validation failed)
- **Solution:** Check variable placeholders and values

### ❌ 404 Not Found
- **Cause:** Incorrect URL or baseUrl
- **Solution:** Verify `baseUrl` environment variable (default: http://localhost:8080)

### ❌ Connection Refused
- **Cause:** Service not running
- **Solution:** Start service with `mvn spring-boot:run`

---

## 📚 Request Body Validation Rules

### Password Requirements
- **Length:** 8-128 characters
- **Recommended:** Mix of uppercase, lowercase, numbers, special characters

### Email Format
- Must be valid email format
- Examples: `user@example.com`, `john.doe@company.co.uk`

### Name Format
- **Length:** 2-120 characters
- Can contain letters, numbers, spaces, hyphens, underscores

---

## 🔐 Security Notes

⚠️ **For Local Development Only:**
- Tokens expire in **15 minutes** (configurable in `application.yml`)
- Refresh tokens valid for **7 days** (configurable in `SecurityProperties`)
- Database stored with encrypted passwords (BCrypt with strength 12)

⚠️ **In Production:**
- Use HTTPS only
- Rotate refresh tokens regularly
- Implement rate limiting on auth endpoints
- Store tokens securely (not in plain localStorage)

---

## 📞 Support

For issues or questions:
1. Check application logs: `tail -f logs/spring.log`
2. Verify database connection: `docker logs postgres` (if using Docker)
3. Review endpoint documentation in code: `/src/main/java/com/atous/auth/presentation/controller/AuthController.java`

---

## 📝 Version History

- **v1.0.0** - Initial collection with complete auth flow
  - Authentication endpoints
  - Password management
  - Token validation
  - Health checks
  - Pre-built test scripts
  - Automated variable management

---

**Last Updated:** 2026-06-22
**Collection Version:** 1.0.0
**API Version:** v1
