package com.example.popularmovieslibrary.model;

public class MovieReview {

    private String mReviewAuthor;
    private String mReviewDetail;

    public MovieReview(String reviewAuthor, String reviewDetail) {
        mReviewAuthor = reviewAuthor;
        mReviewDetail = reviewDetail;
    }

    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    public String getReviewDetail() {
        return mReviewDetail;
    }
}
