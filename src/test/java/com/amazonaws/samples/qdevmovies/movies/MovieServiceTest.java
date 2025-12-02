package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! Unit tests for our MovieService treasure chest operations
 * Testing all the search functionality like a proper pirate crew!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should return all movies when no search criteria provided")
    public void testSearchMoviesWithNoCriteria() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        assertNotNull(results);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    @DisplayName("Should find movies by exact name match")
    public void testSearchMoviesByExactName() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should find movies by partial name match (case insensitive)")
    public void testSearchMoviesByPartialName() {
        List<Movie> results = movieService.searchMovies("prison", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should find movies by case insensitive name")
    public void testSearchMoviesByCaseInsensitiveName() {
        List<Movie> results = movieService.searchMovies("PRISON ESCAPE", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should find movie by exact ID")
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    @DisplayName("Should find movies by genre (case insensitive)")
    public void testSearchMoviesByGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "drama");
        
        assertNotNull(results);
        assertTrue(results.size() > 0);
        assertTrue(results.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    @DisplayName("Should find movies by partial genre match")
    public void testSearchMoviesByPartialGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "crime");
        
        assertNotNull(results);
        assertTrue(results.size() > 0);
        assertTrue(results.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("crime")));
    }

    @Test
    @DisplayName("Should find movies matching multiple criteria")
    public void testSearchMoviesWithMultipleCriteria() {
        List<Movie> results = movieService.searchMovies("family", null, "crime");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());
        assertTrue(results.get(0).getGenre().toLowerCase().contains("crime"));
    }

    @Test
    @DisplayName("Should return empty list when no movies match criteria")
    public void testSearchMoviesNoMatches() {
        List<Movie> results = movieService.searchMovies("NonExistentMovie", null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when searching with invalid ID")
    public void testSearchMoviesWithInvalidId() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty string search criteria")
    public void testSearchMoviesWithEmptyStrings() {
        List<Movie> results = movieService.searchMovies("", null, "");
        
        assertNotNull(results);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    @DisplayName("Should handle whitespace-only search criteria")
    public void testSearchMoviesWithWhitespaceOnly() {
        List<Movie> results = movieService.searchMovies("   ", null, "   ");
        
        assertNotNull(results);
        assertEquals(movieService.getAllMovies().size(), results.size());
    }

    @Test
    @DisplayName("Should find movies by name only using dedicated method")
    public void testSearchMoviesByNameOnly() {
        List<Movie> results = movieService.searchMoviesByName("prison");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should return empty list for null name in searchMoviesByName")
    public void testSearchMoviesByNameWithNull() {
        List<Movie> results = movieService.searchMoviesByName(null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for empty name in searchMoviesByName")
    public void testSearchMoviesByNameWithEmpty() {
        List<Movie> results = movieService.searchMoviesByName("");
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should find movies by genre only using dedicated method")
    public void testSearchMoviesByGenreOnly() {
        List<Movie> results = movieService.searchMoviesByGenre("drama");
        
        assertNotNull(results);
        assertTrue(results.size() > 0);
        assertTrue(results.stream().allMatch(movie -> 
            movie.getGenre().toLowerCase().contains("drama")));
    }

    @Test
    @DisplayName("Should return empty list for null genre in searchMoviesByGenre")
    public void testSearchMoviesByGenreWithNull() {
        List<Movie> results = movieService.searchMoviesByGenre(null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for empty genre in searchMoviesByGenre")
    public void testSearchMoviesByGenreWithEmpty() {
        List<Movie> results = movieService.searchMoviesByGenre("");
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should maintain existing getMovieById functionality")
    public void testGetMovieByIdStillWorks() {
        Optional<Movie> result = movieService.getMovieById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("The Prison Escape", result.get().getMovieName());
    }

    @Test
    @DisplayName("Should return empty optional for invalid ID in getMovieById")
    public void testGetMovieByIdWithInvalidId() {
        Optional<Movie> result = movieService.getMovieById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for null ID in getMovieById")
    public void testGetMovieByIdWithNullId() {
        Optional<Movie> result = movieService.getMovieById(null);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for zero ID in getMovieById")
    public void testGetMovieByIdWithZeroId() {
        Optional<Movie> result = movieService.getMovieById(0L);
        
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return empty optional for negative ID in getMovieById")
    public void testGetMovieByIdWithNegativeId() {
        Optional<Movie> result = movieService.getMovieById(-1L);
        
        assertFalse(result.isPresent());
    }
}