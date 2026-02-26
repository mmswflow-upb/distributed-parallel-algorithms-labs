# SimpleMovieSpringAngular

A separate Spring Boot + Angular CRUD app in a movie context.

## Features
- Create, list, update, delete movies
- Search movies by title
- Mark a movie as watched/unwatched
- Delete all movies

## Data model
Each movie has:
- `id`
- `title`
- `director`
- `watched`

## Backend (Spring Boot)
Path: `SimpleMovieSpringAngular/backend`

Run:
```bash
cd SimpleMovieSpringAngular/backend
mvn spring-boot:run
```
Runs on `http://localhost:8081`.

REST API:
- `POST /api/movies`
- `GET /api/movies`
- `GET /api/movies/:id`
- `PUT /api/movies/:id`
- `DELETE /api/movies/:id`
- `DELETE /api/movies`
- `GET /api/movies?title=keyword`
- `GET /api/movies/watched`

## Frontend (Angular)
Path: `SimpleMovieSpringAngular/frontend`

Run:
```bash
cd SimpleMovieSpringAngular/frontend
npm install
npx ng serve
```
Open `http://localhost:4200`.

## Notes
- Backend storage is in-memory for simplicity.
- Data is reset when the backend restarts.
