# SimpleMovieSpringAngular (Dockerized)

Full stack:
- Spring Boot backend: `backend` (port `8081`)
- Angular frontend: `frontend` (port `4201`)
- MongoDB: `movie_db` (host port `27018` -> container `27017`)

## Quick Start (No local dependency install)
From `Lab_Tasks/SimpleMovieSpringAngular`:

```bash
docker compose up --build
```

Open:
- Frontend: `http://localhost:4201`
- Backend API: `http://localhost:8081/api/movies`
- MongoDB host port: `27018`

## Stop
```bash
docker compose down
```

## Stop + remove DB volume
```bash
docker compose down -v
```

## Notes
- Backend is now MongoDB-backed (not in-memory).
- Backend uses `MONGODB_URI` from compose: `mongodb://mongo:27017/movie_db`.
- Frontend calls backend at `http://localhost:8081`.
