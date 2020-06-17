package com.example.popularmovieslibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.popularmovieslibrary.model.MovieReview;
import com.example.popularmovieslibrary.model.MoviesLab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MJsonUtils {
    private static final String MOVIE_RESULTS = "results";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_BACKDROP = "backdrop_path";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_SYNOPSIS = "overview";
    private static final String MOVIE_ID = "id";

    public static List<MoviesLab> parseMoviesLabJson(String moviesLabJson) {

        String title;
        String release_date;
        String poster_path;
        String backdrop_path;
        String vote_average;
        String overview;
        String id;
        List<MoviesLab> moviesList = new ArrayList<>();
        // Return early
        if (TextUtils.isEmpty(moviesLabJson)) {
            return null;
        }
        try{
            JSONObject jsonMovieObj = new JSONObject(moviesLabJson);

            JSONArray resultsArray = jsonMovieObj.getJSONArray(MOVIE_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++){


                JSONObject firstResults = resultsArray.getJSONObject(i);
                title = firstResults.getString(MOVIE_TITLE);
                release_date= firstResults.getString(MOVIE_RELEASE_DATE);
                poster_path= firstResults.getString(MOVIE_POSTER);
                backdrop_path = firstResults.getString(MOVIE_BACKDROP);
                vote_average= firstResults.getString(MOVIE_RATING);
                overview = firstResults.getString(MOVIE_SYNOPSIS);
                id = firstResults.getString(MOVIE_ID);

                MoviesLab moviesLab = new MoviesLab(title, release_date, poster_path, backdrop_path, vote_average, overview, id);
                moviesList.add(moviesLab);
            } 
        }catch (JSONException e){
            Log.d(TAG, "parseMoviesLabJson() returned: " + moviesList);
        }
        
        return moviesList;
    }

    //All Video Link of Movie
    public static List<String> fetchMovieVideoFromJson(String moviesLabJson) throws JSONException {
        List<String> movieVideo = new ArrayList<>();
            JSONObject baseJsonObject = new JSONObject(moviesLabJson);
            JSONArray jsonArray = baseJsonObject.getJSONArray(MOVIE_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.getString("key");
                String link = "https://www.youtube.com/watch?v=" + key;
                movieVideo.add(link);


            }

        return movieVideo;
    }

    //All Review of Movie
    public static List<MovieReview> fetchMovieReviewFromJson(String moviesLabJson) throws JSONException {
        List<MovieReview> movieReviews = new ArrayList<>();

            JSONObject baseJsonObject = new JSONObject(moviesLabJson);
            JSONArray jsonArray = baseJsonObject.getJSONArray(MOVIE_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                MovieReview movieReview = new MovieReview(author, content);

                movieReviews.add(movieReview);
            }

        return movieReviews;
    }
}
