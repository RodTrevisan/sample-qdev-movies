package com.amazonaws.samples.qdevmovies.utils;

public class MovieIconUtils {
    
    public static String getMovieIcon(String movieName) {
        switch (movieName.toLowerCase()) {
            case "the prison escape": return "🔒";
            case "the family boss": return "👔";
            case "the masked hero": return "🦇";
            case "urban stories": return "🌆";
            case "life journey": return "🏃";
            case "dream heist": return "💭";
            case "the virtual world": return "🕶️";
            case "the wise guys": return "🤵";
            case "the quest for the ring": return "💍";
            case "space wars: the beginning": return "🚀";
            case "the factory owner": return "🏭";
            case "underground club": return "👊";
            default: return "🎬";
        }
    }
}
