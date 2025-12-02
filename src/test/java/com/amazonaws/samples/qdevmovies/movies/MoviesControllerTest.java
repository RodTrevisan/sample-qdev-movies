package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Unit tests for our MoviesController treasure map operations
 * Testing all endpoints like a proper pirate crew!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with test data
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                    new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                } else if (id == 2L) {
                    return Optional.of(new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0));
                }
                return Optional.empty();
            }

            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (id != null && !movie.getId().equals(id)) {
                        matches = false;
                    }
                    
                    if (name != null && !name.trim().isEmpty()) {
                        if (!movie.getMovieName().toLowerCase().contains(name.trim().toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        if (!movie.getGenre().toLowerCase().contains(genre.trim().toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }

            @Override
            public List<Movie> searchMoviesByName(String name) {
                if (name == null || name.trim().isEmpty()) {
                    return new ArrayList<>();
                }
                return searchMovies(name, null, null);
            }

            @Override
            public List<Movie> searchMoviesByGenre(String genre) {
                if (genre == null || genre.trim().isEmpty()) {
                    return new ArrayList<>();
                }
                return searchMovies(null, null, genre);
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    // Existing tests
    @Test
    @DisplayName("Should return movies template for getMovies")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
    }

    @Test
    @DisplayName("Should return movie-details template for valid movie ID")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    @DisplayName("Should return error template for invalid movie ID")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    @Test
    @DisplayName("Should integrate with movie service correctly")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    // New search functionality tests
    @Test
    @DisplayName("Should return movies template for search with no criteria")
    public void testSearchMoviesNoCriteria() {
        String result = moviesController.searchMovies(null, null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertEquals(true, model.getAttribute("searchPerformed"));
    }

    @Test
    @DisplayName("Should search movies by name successfully")
    public void testSearchMoviesByName() {
        String result = moviesController.searchMovies("Test", null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchName"));
        assertEquals("Test", model.getAttribute("searchName"));
        assertTrue(model.containsAttribute("searchMessage"));
    }

    @Test
    @DisplayName("Should search movies by ID successfully")
    public void testSearchMoviesById() {
        String result = moviesController.searchMovies(null, 1L, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchId"));
        assertEquals("1", model.getAttribute("searchId"));
    }

    @Test
    @DisplayName("Should search movies by genre successfully")
    public void testSearchMoviesByGenre() {
        String result = moviesController.searchMovies(null, null, "Drama", model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchGenre"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Should handle search with no results")
    public void testSearchMoviesNoResults() {
        String result = moviesController.searchMovies("NonExistent", null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("noResults"));
        assertEquals(true, model.getAttribute("noResults"));
        assertTrue(model.containsAttribute("searchMessage"));
        String message = (String) model.getAttribute("searchMessage");
        assertTrue(message.contains("Arrr! No treasure found"));
    }

    @Test
    @DisplayName("Should return successful API response for search with results")
    public void testSearchMoviesApiWithResults() {
        ResponseEntity<MoviesController.MovieSearchResponse> response = 
            moviesController.searchMoviesApi("Test", null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalResults());
        assertFalse(response.getBody().isError());
        assertTrue(response.getBody().getMessage().contains("Found 1 movie"));
    }

    @Test
    @DisplayName("Should return API response with no results")
    public void testSearchMoviesApiNoResults() {
        ResponseEntity<MoviesController.MovieSearchResponse> response = 
            moviesController.searchMoviesApi("NonExistent", null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalResults());
        assertFalse(response.getBody().isError());
        assertTrue(response.getBody().getMessage().contains("No treasure found"));
    }
}
