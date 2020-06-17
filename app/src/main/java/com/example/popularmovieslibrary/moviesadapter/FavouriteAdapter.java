package com.example.popularmovieslibrary.moviesadapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.popularmovieslibrary.model.FavouriteDetails;
import com.example.popularmovieslibrary.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<FavouriteDetails> mList = new ArrayList<>();

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_layout, parent, false);
        return new FavouriteViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.date.setText("Release Date: "+mList.get(position).getDate());
        holder.description.setText(mList.get(position).getDescription());
        holder.rating.setText("Ratings: "+mList.get(position).getRating());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<FavouriteDetails> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public FavouriteDetails getItemAt(int position) {
        return mList.get(position);
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView date;
        private TextView rating;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.favourite_title);
            date = itemView.findViewById(R.id.favourite_date);
            description = itemView.findViewById(R.id.favourite_description);
            rating = itemView.findViewById(R.id.favourite_rating);
        }
    }
}
