# Movie Service - Spring Boot Demo Application

A simple movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a swashbuckling pirate theme! Arrr!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🏴‍☠️ Movie Search**: Search through our treasure chest of movies by name, ID, or genre with pirate-themed interface
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **REST API**: JSON endpoints for programmatic access to movie data and search functionality

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for server-side templating

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **🏴‍☠️ Movie Search**: http://localhost:8080/movies/search (with optional query parameters)

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── MovieService.java         # Business logic with search functionality
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── MovieIconUtils.java   # Movie icon utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── application.yml               # Application configuration
│       ├── movies.json                   # Movie data treasure chest
│       ├── mock-reviews.json             # Mock review data
│       ├── log4j2.xml                    # Logging configuration
│       ├── templates/                    # Thymeleaf templates
│       │   ├── movies.html              # Movie list with search form
│       │   └── movie-details.html       # Movie details page
│       └── static/css/                   # Pirate-themed styling
└── test/                                 # Comprehensive unit tests
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings and basic information, including a pirate-themed search form.

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

### 🏴‍☠️ Search Movies (HTML Interface)
```
GET /movies/search
```
Returns an HTML page with search results using the same movie grid layout. Perfect for landlubbers using browsers!

**Query Parameters (all optional):**
- `name` (string): Movie name to search for (case-insensitive partial match)
- `id` (number): Movie ID to search for (exact match)
- `genre` (string): Movie genre to search for (case-insensitive partial match)

**Examples:**
```
http://localhost:8080/movies/search?name=prison
http://localhost:8080/movies/search?genre=drama
http://localhost:8080/movies/search?id=1
http://localhost:8080/movies/search?name=family&genre=crime
```

### 🏴‍☠️ Search Movies (JSON API)
```
GET /movies/search/api
```
Returns search results as JSON for ye API-savvy pirates! Perfect for when ye need raw data without the fancy HTML decorations.

**Query Parameters (all optional):**
- `name` (string): Movie name to search for (case-insensitive partial match)
- `id` (number): Movie ID to search for (exact match)
- `genre` (string): Movie genre to search for (case-insensitive partial match)

**Response Format:**
```json
{
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    }
  ],
  "totalResults": 1,
  "message": "Ahoy! Found 1 movie in our treasure chest!",
  "searchCriteria": {
    "name": "prison",
    "id": null,
    "genre": null
  },
  "error": false
}
```

**Examples:**
```
http://localhost:8080/movies/search/api?name=prison
http://localhost:8080/movies/search/api?genre=drama
http://localhost:8080/movies/search/api?id=1
```

## Search Features

### 🏴‍☠️ Pirate-Themed Search Interface
- **Visual Design**: Brown and gold color scheme with pirate flag emoji
- **Pirate Language**: Search messages use authentic pirate terminology
- **Responsive Form**: Works on all devices with mobile-optimized layout
- **Real-time Feedback**: Shows search results count and helpful messages

### Search Capabilities
- **Name Search**: Case-insensitive partial matching (e.g., "prison" finds "The Prison Escape")
- **ID Search**: Exact match by movie ID
- **Genre Search**: Case-insensitive partial matching (e.g., "crime" finds "Crime/Drama")
- **Combined Search**: Use multiple criteria together for precise results
- **Empty Results Handling**: Friendly pirate messages when no movies match

### Edge Cases Handled
- **Invalid Parameters**: Graceful handling of invalid IDs or empty strings
- **No Results**: Clear messaging when search yields no results
- **Server Errors**: Proper error handling with user-friendly messages
- **Input Validation**: Trimming whitespace and handling null values

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the treasure chest
- Enhance the UI/UX with more pirate themes
- Add new search features like filtering by year or rating
- Improve the responsive design
- **🏴‍☠️ New Feature**: The search functionality demonstrates advanced Spring Boot features including:
  - Query parameter handling
  - Service layer business logic
  - Comprehensive error handling
  - Both HTML and JSON API responses
  - Extensive unit test coverage

## Testing

Run the comprehensive test suite:
```bash
mvn test
```

The test suite includes:
- **MovieServiceTest**: Tests for all search functionality and edge cases
- **MoviesControllerTest**: Tests for both HTML and API endpoints
- **Integration Tests**: End-to-end testing of search features
- **Edge Case Coverage**: Null handling, empty results, invalid parameters

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

**Ahoy matey!** This movie service now includes a fully functional search feature with a swashbuckling pirate theme. Search through our treasure chest of movies and may ye find the perfect film for yer viewing pleasure! 🏴‍☠️
