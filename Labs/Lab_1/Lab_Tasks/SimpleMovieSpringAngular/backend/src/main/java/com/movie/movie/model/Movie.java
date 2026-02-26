package com.movie.movie.model;

public class Movie {
    private String id;

    private String title;
    private String director;
    private boolean watched;

    public Movie() {

    }

    public Movie(String title, String director, boolean watched) {
        this.title = title;
        this.director = director;
        this.watched = watched;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", director=" + director + ", watched=" + watched + "]";
    }
}
