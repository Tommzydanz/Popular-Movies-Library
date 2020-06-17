package com.example.popularmovieslibrary.model;

public class MoviesLab{
    private String movieTitle;
    private String movieReleaseDate;
    private String movieImagePoster;
    private String movieImageBackdrop;
    private String voteAverage;
    private String movieSynopsis;
    private String movieId;

    public MoviesLab(String movieTitle, String movieReleaseDate, String movieImagePoster, String movieImageBackdrop, String voteAverage, String movieSynopsis, String movieId){
        this.movieTitle = movieTitle;
        this.movieReleaseDate =movieReleaseDate;
        this.movieImagePoster = movieImagePoster;
        this.movieImageBackdrop = movieImageBackdrop;
        this.voteAverage = voteAverage;
        this.movieSynopsis = movieSynopsis;
        this.movieId = movieId;

    }
    public  String getMovieId(){
        return movieId;
    }
    // movie title
    public String getMovieTitle() {
        return movieTitle;
    }
    // movie release date
    public String getMovieReleaseDate(){
        return movieReleaseDate;
    }

    // movie image poster
    public String getMovieImagePoster(){
        return movieImagePoster;
    }

    //movie backdrop Image poster
    public String getMovieImageBackdrop() {
        return movieImageBackdrop;
    }


    // ratings on movie
    public String getVoteAverage(){
        return voteAverage;
    }


    // movie overview
    public String getMovieSynopsis(){
        return movieSynopsis;
    }

}
