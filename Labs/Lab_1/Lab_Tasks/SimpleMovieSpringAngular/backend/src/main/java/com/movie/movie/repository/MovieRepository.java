package com.movie.movie.repository;

import com.movie.movie.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByTitleContaining(String title);
    List<Movie> findByWatched(boolean watched);
}
