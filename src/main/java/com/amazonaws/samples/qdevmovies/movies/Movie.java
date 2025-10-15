package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;

public class Movie {

    private final long id;
    private final String movieName;
    private final String director;
    private final int year;
    private final String genre;
    private final String description;
    private final int duration;
    private final double imdbRating;

    public Movie(long id, String movieName, String director, int year, String genre, String description, int duration, double imdbRating) {
        this.id = id;
        this.movieName = movieName;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        this.imdbRating = imdbRating;
    }

    public long getId() {
        return this.id;
    }

    public String getMovieName() {
        return this.movieName;
    }

    public String getDirector() {
        return this.director;
    }

    public int getYear() {
        return this.year;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getDescription() {
        return this.description;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getImdbRating() {
        return this.imdbRating;
    }

    public String getIcon() {
        return MovieIconUtils.getMovieIcon(this.movieName);
    }
}
