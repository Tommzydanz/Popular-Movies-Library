package com.example.popularmovieslibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovieslibrary.database.FavouritesViewModel;
import com.example.popularmovieslibrary.model.FavouriteDetails;
import com.example.popularmovieslibrary.moviesadapter.CustomMoviesAdapter;
import com.example.popularmovieslibrary.utils.MJsonUtils;
import com.example.popularmovieslibrary.utils.MNetworkUtils;

import com.example.popularmovieslibrary.model.MoviesLab;
import com.example.popularmovieslibrary.moviesadapter.FavouriteAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CustomMoviesAdapter.MovieClickListener {
    RecyclerView mRecyclerView;
    Button reloadBtn;
    ProgressBar mProgressBar;
    TextView mErrorMessage;
    FavouriteAdapter favouriteAdapter;


    private FavouritesViewModel favouritesViewModel;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String queryMovie = "popular";
    private String nameSort = "Popular Movies";
    private List<MoviesLab> mMovies = null;
    private static final String QUERY_CALLBACK = "callbackQuery";
    private static final String CALLBACK_NAMESORT= "callbackNamesort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.main_recyclerView);
        reloadBtn = findViewById(R.id.reload_btn);
        favouritesViewModel = new FavouritesViewModel(this.getApplication());
        favouriteAdapter = new FavouriteAdapter();
        mMovies = new ArrayList<>();


        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mRecyclerView.setHasFixedSize(true);


        mProgressBar = findViewById(R.id.pb_main);
        mErrorMessage = findViewById(R.id.error_message_tv);

        setTitle(nameSort);
        if (isOnline()) {
            errorNetwork();
            return;
        }

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(QUERY_CALLBACK) || savedInstanceState.containsKey(CALLBACK_NAMESORT)){
                queryMovie = savedInstanceState.getString(QUERY_CALLBACK);
                nameSort = savedInstanceState.getString(CALLBACK_NAMESORT);
                setTitle(nameSort);
                new  MovieFetchTask().execute(queryMovie);
                return;
            }
        }

        new MovieFetchTask().execute(queryMovie);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo == null || !netInfo.isConnectedOrConnecting();
    }


    private void errorNetwork() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        reloadBtn.setVisibility(View.VISIBLE);
    }

    public void clickTryAgain(View view) {
        if (isOnline()) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.startAnimation(shake);
            return;
        }
        queryMovie = "popular";
        reloadBtn.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        new MovieFetchTask().execute(queryMovie);
    }

    private void hideProgressBarAndTextView() {
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClickMovie(int position) {

        if (isOnline()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            errorNetwork();
            return;
        }
        Intent intent = new Intent(this, MoviesDetailActivity.class);
        intent.putExtra("title", mMovies.get(position).getMovieTitle());
        intent.putExtra("poster", mMovies.get(position).getMovieImagePoster());
        intent.putExtra("backdrop", mMovies.get(position).getMovieImageBackdrop());
        intent.putExtra("id", mMovies.get(position).getMovieId());
        intent.putExtra("plot", mMovies.get(position).getMovieSynopsis());
        intent.putExtra("rating", mMovies.get(position).getVoteAverage());
        intent.putExtra("releaseDate", mMovies.get(position).getMovieReleaseDate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryMovieSaved = queryMovie;
        String nameSortSaved = nameSort;
        outState.putString(QUERY_CALLBACK, queryMovieSaved);
        outState.putString(CALLBACK_NAMESORT, nameSortSaved);

    }

    @SuppressLint("StaticFieldLeak")
    private class MovieFetchTask extends AsyncTask<String, Void, List<MoviesLab>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                mRecyclerView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected List<MoviesLab> doInBackground(String... strings) {
            if (isOnline()) {
                errorNetwork();
                return null;
            }
            URL movieUrl = MNetworkUtils.buildUrl(strings[0]);

            String movieResponse;
            try {
                movieResponse = MNetworkUtils.getHttpResponse(movieUrl);
                mMovies = MJsonUtils.parseMoviesLabJson(movieResponse);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return mMovies;
        }

        @Override
        protected void onPostExecute(List<MoviesLab> movies) {
            new MovieFetchTask().cancel(true);
            if (movies != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                reloadBtn.setVisibility(View.GONE);
                hideProgressBarAndTextView();
                mMovies = movies;
                CustomMoviesAdapter movieAdapter = new CustomMoviesAdapter(movies, MainActivity.this, MainActivity.this);
                mRecyclerView.setAdapter(movieAdapter);
                if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else{
                    mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                }


            } else {
                Log.e(LOG_TAG, "Problems with adapter ");
            }
        }

    }


    private void removeItemOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                favouritesViewModel.delete(favouriteAdapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "removed from favourites", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void updateFavouriteItems() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(favouriteAdapter);
            favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
            favouritesViewModel.getAllFavourites().observe(this, new Observer<List<FavouriteDetails>>() {
                @Override
                public void onChanged(List<FavouriteDetails> favouriteDetails) {
                    favouriteAdapter.setList(favouriteDetails);
                }
            });
            mRecyclerView.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isOnline()) return false;


        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                queryMovie = "popular";
                new MovieFetchTask().execute(queryMovie);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.top_rated:
                queryMovie = "top_rated";
                new MovieFetchTask().execute(queryMovie);
                nameSort = "Top Rated Movies";
                setTitle(nameSort);
                break;
            case R.id.favourite:
                mProgressBar.setVisibility(View.INVISIBLE);
                nameSort = "My Favourites";
                setTitle(nameSort);
                updateFavouriteItems();
                removeItemOnSwipe();
                break;

        }
        return true;
    }

}