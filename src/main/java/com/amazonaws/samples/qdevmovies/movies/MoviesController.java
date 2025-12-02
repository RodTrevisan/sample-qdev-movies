package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * Ahoy matey! This endpoint searches through our treasure chest of movies
     * Returns HTML page with search results, perfect for landlubbers using browsers!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Movie genre to search for (optional)
     * @param model Spring model for template rendering
     * @return Template name for search results
     */
    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Searching for movies with name: {}, id: {}, genre: {}", name, id, genre);
        
        try {
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            model.addAttribute("movies", searchResults);
            model.addAttribute("searchName", name != null ? name : "");
            model.addAttribute("searchId", id != null ? id.toString() : "");
            model.addAttribute("searchGenre", genre != null ? genre : "");
            model.addAttribute("searchPerformed", true);
            model.addAttribute("resultCount", searchResults.size());
            
            if (searchResults.isEmpty()) {
                model.addAttribute("noResults", true);
                model.addAttribute("searchMessage", "Arrr! No treasure found matching yer search criteria, matey!");
            } else {
                model.addAttribute("searchMessage", 
                    "Ahoy! Found " + searchResults.size() + " movie" + 
                    (searchResults.size() == 1 ? "" : "s") + " in our treasure chest!");
            }
            
            logger.info("Search completed successfully, found {} movies", searchResults.size());
            return "movies";
            
        } catch (Exception e) {
            logger.error("Arrr! Search failed with error: {}", e.getMessage(), e);
            model.addAttribute("title", "Search Failed");
            model.addAttribute("message", "Shiver me timbers! Something went wrong with yer search, try again later!");
            return "error";
        }
    }

    /**
     * Ahoy! This endpoint returns search results as JSON for ye API-savvy pirates!
     * Perfect for when ye need raw data without the fancy HTML decorations.
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Movie genre to search for (optional)
     * @return ResponseEntity with search results as JSON
     */
    @GetMapping("/movies/search/api")
    @ResponseBody
    public ResponseEntity<MovieSearchResponse> searchMoviesApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! API search for movies with name: {}, id: {}, genre: {}", name, id, genre);
        
        try {
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            MovieSearchResponse response = new MovieSearchResponse();
            response.setMovies(searchResults);
            response.setTotalResults(searchResults.size());
            response.setSearchCriteria(new SearchCriteria(name, id, genre));
            
            if (searchResults.isEmpty()) {
                response.setMessage("Arrr! No treasure found matching yer search criteria, matey!");
            } else {
                response.setMessage("Ahoy! Found " + searchResults.size() + " movie" + 
                    (searchResults.size() == 1 ? "" : "s") + " in our treasure chest!");
            }
            
            logger.info("API search completed successfully, found {} movies", searchResults.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Arrr! API search failed with error: {}", e.getMessage(), e);
            
            MovieSearchResponse errorResponse = new MovieSearchResponse();
            errorResponse.setMovies(new ArrayList<>());
            errorResponse.setTotalResults(0);
            errorResponse.setMessage("Shiver me timbers! Something went wrong with yer search, try again later!");
            errorResponse.setError(true);
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Response class for API search results, ship-shape and organized!
     */
    public static class MovieSearchResponse {
        private List<Movie> movies;
        private int totalResults;
        private String message;
        private SearchCriteria searchCriteria;
        private boolean error = false;

        // Getters and setters for our treasure map data
        public List<Movie> getMovies() { return movies; }
        public void setMovies(List<Movie> movies) { this.movies = movies; }
        
        public int getTotalResults() { return totalResults; }
        public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public SearchCriteria getSearchCriteria() { return searchCriteria; }
        public void setSearchCriteria(SearchCriteria searchCriteria) { this.searchCriteria = searchCriteria; }
        
        public boolean isError() { return error; }
        public void setError(boolean error) { this.error = error; }
    }

    /**
     * Search criteria class to keep track of what the user searched for
     */
    public static class SearchCriteria {
        private String name;
        private Long id;
        private String genre;

        public SearchCriteria() {}
        
        public SearchCriteria(String name, Long id, String genre) {
            this.name = name;
            this.id = id;
            this.genre = genre;
        }

        // Getters and setters for search parameters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }
    }
}