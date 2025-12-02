package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! This here method searches through our treasure chest of movies
     * using various criteria like name, id, and genre. Arrr!
     * 
     * @param name Movie name to search for (case-insensitive partial match)
     * @param id Movie ID to search for
     * @param genre Movie genre to search for (case-insensitive partial match)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Searching for movies with name: {}, id: {}, genre: {}", name, id, genre);
        
        return movies.stream()
                .filter(movie -> matchesSearchCriteria(movie, name, id, genre))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a movie matches the search criteria, ye scurvy dog!
     * 
     * @param movie The movie to check
     * @param name Name criteria (null or empty means no filter)
     * @param id ID criteria (null means no filter)
     * @param genre Genre criteria (null or empty means no filter)
     * @return true if movie matches all provided criteria
     */
    private boolean matchesSearchCriteria(Movie movie, String name, Long id, String genre) {
        // Check ID match first (exact match required)
        if (id != null && !movie.getId().equals(id)) {
            return false;
        }
        
        // Check name match (case-insensitive partial match)
        if (name != null && !name.trim().isEmpty()) {
            if (!movie.getMovieName().toLowerCase().contains(name.trim().toLowerCase())) {
                return false;
            }
        }
        
        // Check genre match (case-insensitive partial match)
        if (genre != null && !genre.trim().isEmpty()) {
            if (!movie.getGenre().toLowerCase().contains(genre.trim().toLowerCase())) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Searches movies by name only, perfect for when ye know what treasure ye seek!
     * 
     * @param name Movie name to search for (case-insensitive partial match)
     * @return List of movies matching the name criteria
     */
    public List<Movie> searchMoviesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Arrr! Empty name provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        return searchMovies(name, null, null);
    }

    /**
     * Searches movies by genre only, for when ye want to sail specific waters!
     * 
     * @param genre Movie genre to search for (case-insensitive partial match)
     * @return List of movies matching the genre criteria
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.warn("Arrr! Empty genre provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        return searchMovies(null, null, genre);
    }
}
