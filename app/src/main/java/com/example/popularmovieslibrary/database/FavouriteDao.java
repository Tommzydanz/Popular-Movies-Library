package com.example.popularmovieslibrary.database;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmovieslibrary.model.FavouriteDetails;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavouriteDetails favouriteDetails);

    @Delete
    void delete(FavouriteDetails favouriteDetails);

    @Query("SELECT * FROM favourite_movies_table ORDER BY mId ASC")
    LiveData<List<FavouriteDetails>> getAllFavourite();

}
