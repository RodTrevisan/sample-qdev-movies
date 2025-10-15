package com.amazonaws.samples.qdevmovies.movies;

public class Review {
    private String userName;
    private String avatarEmoji;
    private double rating;
    private String comment;

    public Review(String userName, String avatarEmoji, double rating, String comment) {
        this.userName = userName;
        this.avatarEmoji = avatarEmoji;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserName() { return userName; }
    public String getAvatarEmoji() { return avatarEmoji; }
    public double getRating() { return rating; }
    public String getComment() { return comment; }
}
