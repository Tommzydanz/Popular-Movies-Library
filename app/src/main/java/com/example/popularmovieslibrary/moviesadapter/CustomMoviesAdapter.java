package com.example.popularmovieslibrary.moviesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovieslibrary.R;
import com.example.popularmovieslibrary.model.MoviesLab;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomMoviesAdapter extends RecyclerView.Adapter<CustomMoviesAdapter.MoviesHolder> {

    private static final String URL_IMAGE_PATH = "https://image.tmdb.org/t/p/w500";

    private List<MoviesLab> mMovies;

    private final MovieClickListener mMovieClickListener;
    private Context context;
    public interface MovieClickListener {
        void onClickMovie(int position);
    }


    public CustomMoviesAdapter(List<MoviesLab> moviesLabs, Context context, MovieClickListener movieClickListener) {
        mMovies = moviesLabs;
        this.context = context;
        mMovieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movies_image_poster_list_item, parent, false);

        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder holder, int position) {

        Picasso.get()
                .load(URL_IMAGE_PATH.concat(mMovies.get(position).getMovieImagePoster()))
                .fit()
                .into(holder.imageViewHolder);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();

    }



    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageViewHolder;

        MoviesHolder(View movieView) {
            super(movieView);

            imageViewHolder = movieView.findViewById(R.id.list_item_poster_iv);
            imageViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mMovieClickListener.onClickMovie(clickPosition);

        }


    }


}
