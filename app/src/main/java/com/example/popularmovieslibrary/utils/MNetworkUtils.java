package com.example.popularmovieslibrary.utils;

import android.net.Uri;
import android.util.Log;

import com.example.popularmovieslibrary.model.MovieReview;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class MNetworkUtils {

    /*******
     ***Enter your api key here______"your_api_key"***
     */
    public static final String API_KEY = "";

    // check for code health
    private static final String LOG_TAG = MNetworkUtils.class.getSimpleName();
    //::::::::::::::::::::::::::::::::::::::::::::::::://
    private static final String MOVIE_API_KEY = "api_key";

    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";


    public static URL buildUrl(String movieUrl) {

        Uri uri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath(movieUrl)
                .appendQueryParameter(MOVIE_API_KEY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "url creation problems: ", e);
        }

        return url;
    }
    //creating url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        return url;
    }

    public static List<MovieReview> fetchMovieReview(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = getHttpResponse(url);
        } catch (IOException e) {
           e.printStackTrace();
        }

        return MJsonUtils.fetchMovieReviewFromJson(jsonResponse);

    }

    public static List<String> fetchMovieVideo(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = getHttpResponse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MJsonUtils.fetchMovieVideoFromJson(jsonResponse);

    }

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return getHttpResponse(url);
    }
}