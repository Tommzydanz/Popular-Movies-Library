package com.example.popularmovieslibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovieslibrary.database.FavouritesViewModel;
import com.example.popularmovieslibrary.utils.MNetworkUtils;
import com.example.popularmovieslibrary.R;
import com.example.popularmovieslibrary.model.FavouriteDetails;
import com.example.popularmovieslibrary.model.MovieReview;
import com.example.popularmovieslibrary.moviesadapter.MoviesReviewAdapter;
import com.example.popularmovieslibrary.moviesadapter.MoviesVideoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import java.util.List;

public class MoviesDetailActivity extends AppCompatActivity {

    private static final String URL_IMAGE_PATH = "https://image.tmdb.org/t/p/w185";
    private static final String URL_BACKDROP_PATH = "https://image.tmdb.org/t/p/original";
    private static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
    /******input your Api key here*****/
    private static final String URL_VIDEO_LINK_AND_LANG = "/videos?api_key=
	/**Insert api_key in between the string before the &language=en-US***/
	&language=en-US";
    private static final String URL_REVIEW_LINK_AND_LANG = "/reviews?api_key=
	/**Insert api_key in between the string before the &language=en-US***/
	&language=en-US";
    String  mId, title, overView, date, rating;
    MoviesVideoAdapter movieVideoAdapter;
    RecyclerView videoRecyclerView;
    RecyclerView reviewRecyclerView;
    MoviesReviewAdapter movieReviewAdapter;
    FavouritesViewModel favouritesViewModel;
    FavouriteDetails favouriteDetails;
    int flag = 0;
    List<FavouriteDetails> allFavouriteList;
    ImageView reviewLoadImage;
    ImageView videoLoadImage;

    //Async Tasks
    MovieVideoAsyncTask asyncTaskVideo;
    MovieReviewAsyncTask  asyncTaskReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        updateVideo();
        updateReviews();

        reviewLoadImage = findViewById(R.id.review_load_image);
        videoLoadImage = findViewById(R.id.trailer_load_image);
        reviewLoadImage.setVisibility(View.VISIBLE);
        videoLoadImage.setVisibility(View.VISIBLE);
        Picasso
                .get()
                .load(R.drawable.load5)
                .into(reviewLoadImage);
        Picasso
                .get()
                .load(R.drawable.load5)
                .into(videoLoadImage);


        FloatingActionButton favButton = findViewById(R.id.set_favourite);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    for (int i = 0; i < allFavouriteList.size(); i++) {
                        if (allFavouriteList.get(i).getId() == Integer.parseInt(mId)) {
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        addToFavourite();
                        flag = 1;
                    } else {
                        flag = 0;
                        deleteFromFavourite();
                    }


            }
        });

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouritesViewModel.getAllFavourites().observe(this, new Observer<List<FavouriteDetails>>() {
            @Override
            public void onChanged(List<FavouriteDetails> favouriteDetails) {
                allFavouriteList = favouriteDetails;


            }
        });

        //Updates the details from MovieDetail Intent passed

		// make sure the variables are exactly the same as that of the favouriteDetails
        assert intent != null;
        title = intent.getStringExtra("title");
        String image_poster = getIntent().getStringExtra("poster");
        String backdrop_poster = getIntent().getStringExtra("backdrop");
         overView = intent.getStringExtra("plot");
         rating = intent.getStringExtra("rating");
         date = intent.getStringExtra("releaseDate");
        String id = intent.getStringExtra("id");
        mId = id;


        ImageView movie_poster_iv = findViewById(R.id.movie_poster_iv);
        ImageView backdrop_poster_iv = findViewById(R.id.image_poster_iv);
        assert image_poster != null;
        Picasso.get()
                .load(URL_IMAGE_PATH.concat(image_poster))
                .fit()
                .into(movie_poster_iv);

        assert backdrop_poster != null;
        Picasso.get()
                .load(URL_BACKDROP_PATH.concat(backdrop_poster))
                .fit()
                .into(backdrop_poster_iv);


        TextView title_tv = findViewById(R.id.tv_title);
        title_tv.setText(title);

        TextView plot_synopsis_tv = findViewById(R.id.plot_synopsis_tv);
        plot_synopsis_tv.setText(overView);

        TextView rating_tv = findViewById(R.id.tv_rating);
        assert rating != null;
        rating_tv.setText(rating.concat("/10"));

        TextView release_tv = findViewById(R.id.tv_release);
        release_tv.setText(date);
        setTitle(title);

        asyncTaskVideo = new MovieVideoAsyncTask();
        asyncTaskVideo.execute(getVideoApiLink(mid));

        asyncTaskReview = new MovieReviewAsyncTask();
        asyncTaskReview.execute(getReviewApiLink(mid));

    }

    private void deleteFromFavourite() {
        favouriteDetails= new FavouriteDetails(Integer.parseInt(mId), title, overView, date, rating);
        favouritesViewModel.delete(favouriteDetails);
        Toast.makeText(MoviesDetailActivity.this, "Deleted from favourite", Toast.LENGTH_SHORT).show();
    }

    private void addToFavourite() {

        favouriteDetails= new FavouriteDetails(Integer.parseInt(mId), title, overView, date, rating);
        favouritesViewModel.insert(favouriteDetails);
        Toast.makeText(MoviesDetailActivity.this, "Added to favourite", Toast.LENGTH_SHORT).show();

    }

    //Initiate Reviews View with empty ArrayList
    private void updateReviews() {
        List<MovieReview> movieReviewArrayList = new ArrayList<MovieReview>();
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieReviewAdapter = new MoviesReviewAdapter(movieReviewArrayList, MoviesDetailActivity.this);
        reviewRecyclerView.setAdapter(movieVideoAdapter);
    }

    //Initiate Video View with empty ArrayList
    private void updateVideo() {
        List<String> movieVideoList = new ArrayList<>();
        videoRecyclerView = findViewById(R.id.trailer_recycler_view);
        videoRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        movieVideoAdapter = new MoviesVideoAdapter(movieVideoList, MoviesDetailActivity.this);
        videoRecyclerView.setAdapter(movieVideoAdapter);
    }




    private void closeOnError() {
        finish();
    }

    //Creates VideoApi link from id
    private String getVideoApiLink(String id) {
        return MOVIES_BASE_URL + id  + URL_VIDEO_LINK_AND_LANG;
    }

    //Creates ReviewApi link from id
    private String getReviewApiLink(String id) {
        return MOVIES_BASE_URL + id + URL_REVIEW_LINK_AND_LANG;
    }
    @SuppressLint("StaticFieldLeak")
    public class MovieVideoAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            if (videoRecyclerView.getVisibility() == View.VISIBLE) {
                videoRecyclerView.setVisibility(View.GONE);
            }
            videoLoadImage.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... strings) {


            List<String> movieUrl = null;
            try {
                movieUrl = MNetworkUtils.fetchMovieVideo(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieUrl;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);

            movieVideoAdapter = new MoviesVideoAdapter(list, MoviesDetailActivity.this);
            videoRecyclerView.setAdapter(movieVideoAdapter);

            if (videoRecyclerView.getVisibility() == View.GONE) {
                videoRecyclerView.setVisibility(View.VISIBLE);
            }
            videoLoadImage.setVisibility(View.GONE);

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class MovieReviewAsyncTask extends AsyncTask<String, Void, List<MovieReview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (reviewRecyclerView.getVisibility() == View.VISIBLE) {
                reviewRecyclerView.setVisibility(View.GONE);
            }
            reviewLoadImage.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieReview> doInBackground(String... strings) {
            List<MovieReview> movieUrl = null;
            try {
                movieUrl = MNetworkUtils.fetchMovieReview(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieUrl;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {
            super.onPostExecute(movieReviews);

            movieReviewAdapter = new MoviesReviewAdapter(movieReviews, MoviesDetailActivity.this);
            reviewRecyclerView.setAdapter(movieReviewAdapter);

            if (reviewRecyclerView.getVisibility() == View.GONE) {
                reviewRecyclerView.setVisibility(View.VISIBLE);
                reviewLoadImage.setVisibility(View.GONE);

            }
        }
    }
}