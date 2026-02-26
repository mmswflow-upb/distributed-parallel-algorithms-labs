# Lab_Demo (Dockerized)

Full stack:
- Spring Boot backend: `tutorial` (port `8081`)
- Angular frontend: `AngularCRUD` (port `4200`)
- MongoDB: `angular_db` (port `27017`)

## Quick Start (No local dependency install)
From `Lab_Demo`:

```bash
docker compose up --build
```

Open:
- Frontend: `http://localhost:4200`
- Backend API: `http://localhost:8081/api/tutorials`
- MongoDB host port: `27017`

## Stop
```bash
docker compose down
```

## Stop + remove DB volume
```bash
docker compose down -v
```

## Notes
- Backend uses `MONGODB_URI` from compose: `mongodb://mongo:27017/angular_db`.
- Angular frontend calls backend at `http://localhost:8081`.
