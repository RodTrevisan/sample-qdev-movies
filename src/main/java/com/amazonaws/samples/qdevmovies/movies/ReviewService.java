package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class ReviewService {
    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    public List<Review> getReviewsForMovie(long movieId) {
        List<Review> reviews = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mock-reviews.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONObject reviewsData = new JSONObject(jsonContent);
                if (reviewsData.has(String.valueOf(movieId))) {
                    JSONArray movieReviews = reviewsData.getJSONArray(String.valueOf(movieId));
                    for (int i = 0; i < movieReviews.length(); i++) {
                        JSONObject reviewObj = movieReviews.getJSONObject(i);
                        reviews.add(new Review(
                            reviewObj.getString("userName"),
                            reviewObj.getString("avatarEmoji"),
                            reviewObj.getDouble("rating"),
                            reviewObj.getString("comment")
                        ));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load reviews for movie {}: {}", movieId, e.getMessage());
        }
        return reviews;
    }
}
