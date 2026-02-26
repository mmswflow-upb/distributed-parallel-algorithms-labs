package com.movie.movie.controller;

import com.movie.movie.model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MovieController {

    private final ConcurrentHashMap<String, Movie> movies = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies(@RequestParam(required = false) String title) {
        try {
            List<Movie> filteredMovies = new ArrayList<>();

            this.movies.values().forEach((movie) -> {
                if (title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            });

            if (filteredMovies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(filteredMovies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") String id) {
        Movie movie = movies.get(id);

        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        try {
            Movie newMovie = new Movie(movie.getTitle(), movie.getDirector(), false);
            newMovie.setId(String.valueOf(idSequence.getAndIncrement()));
            movies.put(newMovie.getId(), newMovie);
            return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") String id, @RequestBody Movie movie) {
        Movie existing = movies.get(id);

        if (existing != null) {
            existing.setTitle(movie.getTitle());
            existing.setDirector(movie.getDirector());
            existing.setWatched(movie.isWatched());
            movies.put(id, existing);
            return new ResponseEntity<>(existing, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable("id") String id) {
        try {
            movies.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/movies")
    public ResponseEntity<HttpStatus> deleteAllMovies() {
        try {
            movies.clear();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movies/watched")
    public ResponseEntity<List<Movie>> findWatchedMovies() {
        try {
            List<Movie> watchedMovies = new ArrayList<>();

            this.movies.values().forEach((movie) -> {
                if (movie.isWatched()) {
                    watchedMovies.add(movie);
                }
            });

            if (watchedMovies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(watchedMovies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
