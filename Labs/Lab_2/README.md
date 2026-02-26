# Lab_2 (Dockerized Demo)

This folder contains:
- `SpringAuthentication` (Spring Boot JWT auth backend)
- `AngularAuthetication` (Angular frontend)
- MongoDB (for users/roles data)

## Quick Start
From `Lab_2`:

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

## Stop + remove database volume
```bash
docker compose down -v
```

## Notes
- Backend gets Mongo URL from `MONGODB_URI`.
- Default in compose: `mongodb://mongo:27017/authentication_db`.
- Angular service URLs are configured for backend on `http://localhost:8081`.
- Default roles (`ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`) are auto-seeded on backend startup.

## Useful checks
```bash
docker compose ps
docker compose logs -f backend
docker compose logs -f frontend
```
