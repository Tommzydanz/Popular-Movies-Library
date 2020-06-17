package com.example.popularmovieslibrary.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movies_table")
public class FavouriteDetails {

    @PrimaryKey
    private int mId;
    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mRating;

    public FavouriteDetails(int id,
                            @NonNull String title,
                            @NonNull String description,
                            @NonNull String date,
                            @NonNull String rating) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mRating = rating;

    }


    public int getId() {
        return mId;
    }



    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }


    public String getDate() {
        return mDate;
    }


    public String getRating() {
        return mRating;
    }


}