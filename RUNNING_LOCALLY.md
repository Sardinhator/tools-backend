# Tools Backend - Running Locally

## Quick Start (No Authentication)

The application runs **without authentication by default**, making local development easy:

```bash
# Just run the application - no Keycloak needed!
./gradlew bootRun

# Access the API without authentication
curl http://localhost:8080/api/users

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Running with Keycloak Authentication

If you want to test with Keycloak authentication:

```bash
# 1. Start Keycloak
docker-compose up -d

# 2. Configure Keycloak (see KEYCLOAK_INTEGRATION.md)

# 3. Run with keycloak profile
./gradlew bootRun --args='--spring.profiles.active=keycloak'
```

## Available Profiles

| Profile    | Authentication | Keycloak Required | Use Case                 |
| ---------- | -------------- | ----------------- | ------------------------ |
| (default)  | ❌ Disabled    | No                | Local development        |
| `local`    | ❌ Disabled    | No                | Explicit local dev       |
| `dev`      | ❌ Disabled    | No                | Development without auth |
| `keycloak` | ✅ Enabled     | Yes               | Testing with Keycloak    |
| `test`     | ❌ Disabled    | No                | Automated tests          |

## Feature Flag

Authentication is controlled by:

```yaml
app:
  security:
    authentication:
      enabled: false # true for Keycloak auth
```

See `KEYCLOAK_INTEGRATION.md` for detailed Keycloak setup instructions.
