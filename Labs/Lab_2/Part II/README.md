# Lab 2 - Part II (Dockerized)

JWT Authentication and Authorization demo using:
- `SpringAuthentication` (Spring Boot backend)
- `AngularAuthetication` (Angular frontend)
- MongoDB (`authentication_db`)

## Quick Start
From `Lab_2/Part II`:

```bash
docker compose up --build
```

Open:
- Frontend: `http://localhost:4200`
- Backend API base: `http://localhost:8081/api`
- MongoDB host port: `27019` (container uses `27017`)

## Stop
```bash
docker compose down
```

## Stop + remove DB data
```bash
docker compose down -v
```

## Backend API Endpoints
- `POST /api/auth/signup` - register account
- `POST /api/auth/signin` - login
- `GET /api/test/all` - public content
- `GET /api/test/user` - user content
- `GET /api/test/mod` - moderator content
- `GET /api/test/admin` - admin content

## Notes
- Backend runs on port `8081`.
- Frontend calls backend at `http://localhost:8081`.
- Backend reads Mongo URL from `MONGODB_URI`.
- Compose sets `MONGODB_URI=mongodb://mongo:27017/authentication_db`.
- Default roles are auto-seeded at startup:
  - `ROLE_USER`
  - `ROLE_MODERATOR`
  - `ROLE_ADMIN`

## Default Seeded Accounts
On backend startup, these users are created if missing:
- Moderator
  - username: `moderator`
  - password: `mod12345`
- Admin
  - username: `admin`
  - password: `admin12345`

## How To Test
1. Start the stack:
```bash
docker compose up --build
```
2. Open browser at `http://localhost:4200/home`.
3. Verify public page loads (Home content from `/api/test/all`).
4. Open `http://localhost:4200/login` and login as moderator:
   - username: `moderator`
   - password: `mod12345`
5. After login, navigate to:
   - `http://localhost:4200/user` -> should work
   - `http://localhost:4200/mod` -> should work
   - `http://localhost:4200/admin` -> should show unauthorized message
6. Logout, then login as admin:
   - username: `admin`
   - password: `admin12345`
7. Navigate again:
   - `http://localhost:4200/user` -> should work
   - `http://localhost:4200/admin` -> should work
   - `http://localhost:4200/mod` -> should show unauthorized message
8. Optional signup test:
   - Go to `http://localhost:4200/register`
   - Create a new user account
   - Login with that account and verify only user content is available.

## API Quick Checks (optional)
Public endpoint:
```bash
curl http://localhost:8081/api/test/all
```

Login endpoint:
```bash
curl -X POST http://localhost:8081/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin12345"}'
```

## Useful Commands
```bash
docker compose ps
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f mongo
```

## Common Issues
If frontend starts but login/register fails:
1. Ensure backend is up: `docker compose ps`
2. Check backend logs: `docker compose logs -f backend`
3. Verify backend is reachable at `http://localhost:8081/api/test/all`
