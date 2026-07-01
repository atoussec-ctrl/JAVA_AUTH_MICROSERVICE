# Auth Service

Microservice de autenticação em **Java 21 + Spring Boot 4.1 + Maven**, seguindo **Clean Architecture**, **SOLID**, **Repository Pattern**, **DTOs `record`**, **Ports & Adapters**, **JWT RSA**, **refresh token rotation**, **RBAC**, **Flyway** e **PostgreSQL**.

## Como executar

```bash
docker compose up -d postgres
mvn spring-boot:run
```

## Como testar

```bash
mvn test
```

> O ambiente onde este ZIP foi gerado possui Java 21, mas não possui Maven instalado. Por isso, a validação completa com `mvn test` precisa ser executada localmente ou no CI.

## Endpoints

```txt
POST   /api/v1/auth/register
POST   /api/v1/auth/login
POST   /api/v1/auth/refresh
POST   /api/v1/auth/logout
POST   /api/v1/auth/validate
POST   /api/v1/auth/forgot-password
POST   /api/v1/auth/reset-password

GET    /api/v1/me
POST   /api/v1/me/change-password

POST   /api/v1/me/mfa/enable
POST   /api/v1/me/mfa/verify
POST   /api/v1/me/mfa/disable

GET    /api/v1/admin/users
PATCH  /api/v1/admin/users/{userId}/status
PUT    /api/v1/admin/users/{userId}/roles

POST   /api/v1/admin/roles
PUT    /api/v1/admin/roles/{roleId}
DELETE /api/v1/admin/roles/{roleId}
```

## Observações de produção

- Substitua `JwtKeyConfig`, que gera chave RSA em runtime, por chave vinda de Vault/KMS/Secrets Manager.
- O refresh token é persistido como SHA-256. Para produção crítica, prefira HMAC-SHA-256 com pepper em secret manager.
- O rate limiter deste MVP é in-memory. Em múltiplas instâncias, use Redis.
- O MFA está com adapter de desenvolvimento. Substitua por TOTP real.
