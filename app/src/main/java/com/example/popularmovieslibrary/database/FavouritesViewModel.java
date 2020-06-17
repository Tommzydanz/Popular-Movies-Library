package com.example.popularmovieslibrary.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.popularmovieslibrary.model.FavouriteDetails;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private FavouriteRepository mFavouriteRepository;
    private LiveData<List<FavouriteDetails>> mAllFavourites;

    public FavouritesViewModel(Application application) {
        super(application);
        mFavouriteRepository = new FavouriteRepository(application);
        mAllFavourites = mFavouriteRepository.getAllFavourites();
    }

    public void insert(FavouriteDetails favouriteDetails) {
        mFavouriteRepository.insert(favouriteDetails);
    }

    public void delete(FavouriteDetails favouriteDetails) {
        mFavouriteRepository.delete(favouriteDetails);
    }


    public LiveData<List<FavouriteDetails>> getAllFavourites() {
        return mAllFavourites;
    }

}
