# Keycloak SSO Integration Guide

## 🎯 Objective

Implement Single Sign-On (SSO) authentication with Keycloak for the tools-backend application.

## ✅ Implementation Summary

### 1. Dependencies Configuration

- Added `spring-boot-starter-oauth2-client` dependency in `build.gradle`

### 2. Docker Infrastructure

- Created `docker-compose.yml` with:
  - Keycloak service on port **8180** (admin/admin)
  - MySQL service for Keycloak
  - Shared network `tools-network`

### 3. Spring Configuration

- Moved OAuth2 configuration to `application-dev.yaml` (dev profile only)
- OAuth2 Configuration:
  - Client ID: `tools-backend`
  - Issuer URI: `http://localhost:8180/realms/horasphere`
  - Redirect URI configured automatically
  - Scopes: openid, profile, email

### 4. Security

- Updated `SecurityConfig.java`:
  - Added `@Profile("!test")` to exclude in test mode
  - Configured OAuth2 login with redirect to Swagger
  - Configured logout
  - Public endpoints: Swagger, H2 console, login, oauth2
  - All other endpoints require authentication

### 5. Tests

- Created `TestSecurityConfig.java` for tests (test profile)
  - Disables OAuth2 in test mode
  - Permits all access (permitAll)
- Modified `UserControllerIntegrationTest`:
  - Excluded `OAuth2ClientAutoConfiguration`
  - Imported `TestSecurityConfig`
- **Result**: All tests pass ✅ (19 tests)

### 6. User Synchronization

- Created `KeycloakUserSyncService.java`:
  - Method `syncUserFromKeycloak()`: synchronizes OAuth2 user with database
  - Extracts Keycloak attributes (sub, email, given_name, family_name)
  - Automatically creates user if it doesn't exist

### 7. Documentation

- Created comprehensive setup documentation
- Quick start instructions
- Realm creation guide
- OAuth2 client configuration
- Client secret retrieval
- Test user creation
- Troubleshooting section

## 🔑 Authentication Feature Flag

The application supports **optional authentication** through a feature flag. This allows you to run the application locally without Keycloak.

### Available Profiles

1. **local** (default) - Authentication **disabled**

   - No Keycloak required
   - All endpoints are publicly accessible
   - Perfect for local development and testing

2. **dev** - Authentication **enabled**

   - Requires Keycloak running
   - OAuth2 authentication with Keycloak
   - Protected endpoints require login

3. **test** - Authentication **disabled**
   - Used by automated tests
   - No Keycloak required

### Running Without Authentication (Default)

```bash
# Option 1: No profile specified (uses local by default)
./gradlew bootRun

# Option 2: Explicitly use local profile
./gradlew bootRun --args='--spring.profiles.active=local'

# Access the API without authentication
curl http://localhost:8080/api/users
```

### Running With Authentication

```bash
# Use dev profile to enable Keycloak authentication
./gradlew bootRun --args='--spring.profiles.active=dev'

# Make sure Keycloak is running first
docker-compose up -d
```

### Configuration

The feature flag is controlled by the property:

```yaml
app:
  security:
    authentication:
      enabled: true # or false
```

- `application.yaml` - Default: `enabled: false` (no auth)
- `application-local.yaml` - Explicitly sets: `enabled: false`
- `application-dev.yaml` - Sets: `enabled: true` (requires Keycloak)

## 🚀 Quick Start Guide (With Keycloak)

### Step 1: Start Keycloak

```bash
cd /Users/serge.o.sardinha/workspace/tools-backend
docker-compose up -d
```

Keycloak will be accessible at: http://localhost:8180

### Step 2: Login to Keycloak Admin Console

- URL: http://localhost:8180
- Username: `admin`
- Password: `admin`

### Step 3: Create Realm

1. Click on the "master" dropdown in the top left corner
2. Click on "Create Realm"
3. **Realm name**: `horasphere`
4. Click "Create"

### Step 4: Create Client

1. In the `horasphere` realm, go to **Clients**
2. Click "Create client"
3. Client configuration:
   - **Client ID**: `tools-backend`
   - **Client type**: OpenID Connect
   - Click "Next"
4. Capability config:
   - ✅ **Client authentication**: ON
   - ✅ **Authorization**: OFF
   - ✅ **Standard flow**: ON
   - ✅ **Direct access grants**: ON
   - Click "Next"
5. Login settings:
   - **Root URL**: `http://localhost:8080`
   - **Home URL**: `http://localhost:8080`
   - **Valid redirect URIs**: `http://localhost:8080/*`
   - **Valid post logout redirect URIs**: `http://localhost:8080/*`
   - **Web origins**: `http://localhost:8080`
   - Click "Save"

### Step 5: Get Client Secret

1. In your `tools-backend` client, go to the **Credentials** tab
2. Copy the **Client secret**
3. Update `src/main/resources/application-dev.yaml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-secret: YOUR_CLIENT_SECRET_HERE
```

### Step 6: Create Test User

1. In the `horasphere` realm, go to **Users**
2. Click "Create new user"
3. Configuration:
   - **Username**: `test.user`
   - **Email**: `test.user@horasphere.com`
   - **First name**: `Test`
   - **Last name**: `User`
   - ✅ **Email verified**: ON
   - ✅ **Enabled**: ON
4. Click "Create"
5. Go to the **Credentials** tab
6. Click "Set password"
   - **Password**: `password123`
   - **Password confirmation**: `password123`
   - ❌ **Temporary**: OFF
7. Click "Save"

### Step 7: Create Roles (Optional)

1. In the `horasphere` realm, go to **Realm roles**
2. Create the following roles:
   - `admin`
   - `user`
   - `developer`

### Step 8: Assign Roles to User

1. Go back to **Users** → Your test user
2. Go to the **Role mapping** tab
3. Click "Assign role"
4. Select the roles to assign
5. Click "Assign"

### Step 9: Start the Application

```bash
./gradlew bootRun
```

### Step 10: Test Authentication

- Access http://localhost:8080/swagger-ui.html
- You will be redirected to Keycloak for authentication
- After login, you'll be redirected back to Swagger

### Step 11: Verify User Synchronization

After login, check your MySQL database:

```sql
SELECT * FROM users WHERE email = 'test.user@horasphere.com';
```

The user should be automatically created with the Keycloak ID.

## 📁 Files Modified/Created

### New Files

- `docker-compose.yml` - Keycloak and MySQL containers
- `src/main/resources/application-dev.yaml` - OAuth2 configuration
- `src/main/java/com/intelcom/hora_tools/hora_tools/service/KeycloakUserSyncService.java` - User synchronization service
- `src/test/java/com/intelcom/hora_tools/hora_tools/config/TestSecurityConfig.java` - Test security configuration
- `KEYCLOAK_INTEGRATION.md` - This comprehensive documentation

### Modified Files

- `build.gradle` - Added OAuth2 dependency
- `src/main/resources/application.yaml` - Removed OAuth2 config (moved to application-dev.yaml)
- `src/main/java/com/intelcom/hora_tools/hora_tools/config/SecurityConfig.java` - Added OAuth2 login + @Profile("!test")
- `src/test/resources/application-test.yaml` - Cleanup, removed OAuth2 config
- `src/test/java/com/intelcom/hora_tools/hora_tools/controller/UserControllerIntegrationTest.java` - Excluded OAuth2 + imported TestSecurityConfig

## 🔒 Security Configuration

### Authentication Modes

The application supports two security modes controlled by the feature flag:

#### Mode 1: Authentication Disabled (Default - local profile)

**All endpoints are public** - No authentication required

- `/api/**` - All REST APIs
- `/h2-console/**` - H2 Console
- `/swagger-ui/**` - Swagger Interface
- `/v3/api-docs/**` - OpenAPI Documentation

#### Mode 2: Authentication Enabled (dev profile)

**Public Endpoints** (No Authentication Required):

- `/h2-console/**` - H2 Console
- `/swagger-ui/**` - Swagger Interface
- `/swagger-ui.html` - Main Swagger Page
- `/v3/api-docs/**` - OpenAPI Documentation
- `/api-docs/**` - API Documentation
- `/login/**` - Login Pages
- `/oauth2/**` - OAuth2 Callbacks

**Protected Endpoints** (Authentication Required):

- `/api/**` - All REST APIs (requires Keycloak login)

## 🧪 Testing

- **19 tests pass successfully**
- Tests run with the "test" profile which disables OAuth2
- `TestSecurityConfig` permits all access in test mode
- No need for Keycloak running during tests

## 📝 Important Notes

1. **Spring Profiles**:

   - `local` (default): Authentication disabled, no Keycloak required
   - `dev`: Authentication enabled with Keycloak
   - `test`: Authentication disabled for automated tests

2. **Feature Flag**:

   - Property: `app.security.authentication.enabled`
   - Default: `false` (no authentication)
   - Set to `true` in `application-dev.yaml` for Keycloak integration

3. **Running Locally**:

   - Default behavior: No authentication required
   - Quick start: Just run `./gradlew bootRun`
   - All endpoints accessible without Keycloak

4. **Client Secret**:

   - Configure in `application-dev.yaml`
   - Do not commit to production
   - Use environment variables for production

5. **Field Type**:

   - `isSystemAdmin` is of type `Integer` (not Boolean)
   - Use `0` for false, `1` for true

6. **User Synchronization**:
   - Users are created automatically on first login
   - `keycloakId` (field `sub`) serves as unique identifier
   - Information is updated on each login

## 🎨 Authentication Flow

```text
1. User → http://localhost:8080/api/users
2. Spring Security detects unauthenticated
3. Redirect → http://localhost:8180/realms/horasphere/protocol/openid-connect/auth
4. User logs in on Keycloak
5. Keycloak → Callback → http://localhost:8080/login/oauth2/code/keycloak
6. Spring retrieves OAuth2 token
7. KeycloakUserSyncService.syncUserFromKeycloak() is called
8. User created/updated in database
9. Redirect → http://localhost:8080/swagger-ui.html
10. User authenticated and can access APIs
```

## 📋 Reference URLs

| Service        | URL                                       |
| -------------- | ----------------------------------------- |
| Keycloak Admin | <http://localhost:8180>                   |
| Application    | <http://localhost:8080>                   |
| Swagger UI     | <http://localhost:8080/swagger-ui.html>   |
| API Users      | <http://localhost:8080/api/users>         |
| Keycloak Realm | <http://localhost:8180/realms/horasphere> |

## 🔧 Configuration Files

### application-dev.yaml

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: tools-backend
            client-secret: YOUR_CLIENT_SECRET
            scope: openid,profile,email
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          keycloak:
            issuer-uri: http://localhost:8180/realms/horasphere
            user-name-attribute: preferred_username
```

### docker-compose.yml

```yaml
services:
  keycloak:
    image: quay.io/keycloak/keycloak:23.0
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    ports:
      - "8180:8080"
    networks:
      - tools-network

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: keycloakdb
    ports:
      - "3307:3306"
    networks:
      - tools-network

networks:
  tools-network:
    driver: bridge
```

## 🔍 Troubleshooting

### Issue: Tests fail with Keycloak connection

**Solution**: Verify that `@ActiveProfiles("test")` is present and `TestSecurityConfig` is imported

### Issue: OAuth2 not working in dev

**Solution**:

- Verify Keycloak is started: `docker-compose ps`
- Verify the realm: must be "horasphere"
- Verify the client secret in `application-dev.yaml`

### Issue: Infinite redirect loop

**Solution**: Verify that redirect URLs are configured in Keycloak client

### Issue: "Invalid redirect URI"

**Solution**: Check that in Keycloak Client Settings:

- Valid redirect URIs contains: `http://localhost:8080/*`

### Issue: "Client not found"

**Solution**: Verify that:

- The realm name is exactly `horasphere`
- The client ID is exactly `tools-backend`

### Issue: "Invalid client secret"

**Solution**:

1. Go to Keycloak → Client → Credentials
2. Copy the new secret
3. Update `application-dev.yaml`
4. Restart the application

### Issue: User not syncing to database

**Solution**: Check application logs:

```bash
./gradlew bootRun
```

Look for messages from `KeycloakUserSyncService`.

## 🎯 Next Steps

1. **Configure HTTPS** for production
2. **Add more identity providers** (Google, GitHub, etc.)
3. **Map Keycloak roles** to `user_roles` table
4. **Add group mapping** to `user_group` table
5. **Configure token expiration**
6. **Set up refresh tokens**
7. **Implement role-based access control**

## 📚 Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2)
- [Spring Boot + Keycloak](https://www.baeldung.com/spring-boot-keycloak)

## 🎉 Final Result

✅ Successful build with `./gradlew clean build`  
✅ All tests pass (19/19)  
✅ OAuth2 configured and functional  
✅ Complete documentation  
✅ Dev/test profile separation  
✅ Automatic user synchronization  
✅ Séparation profil dev/test  
✅ Synchronisation utilisateur automatique
