package com.popularmovies.utils;

import android.content.ContentValues;

import com.popularmovies.data.tables.MoviesTable;
import com.popularmovies.data.tables.ReviewsTable;
import com.popularmovies.data.tables.TrailersTable;
import com.popularmovies.entities.Movie;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieTrailer;

public class ContentValuesUtil {

    public static ContentValues getValuesFavoriteMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesTable._ID, movie.getId());
        values.put(MoviesTable.COLUMN_DATE, movie.getDate());
        values.put(MoviesTable.COLUMN_DURATION, movie.getDuration());
        values.put(MoviesTable.COLUMN_POSTER, movie.getPoster());
        values.put(MoviesTable.COLUMN_RATE, movie.getRate());
        values.put(MoviesTable.COLUMN_SYNOPSIS, movie.getSynopsis());
        values.put(MoviesTable.COLUMN_TITLE, movie.getTitle());
        return values;
    }

    public static ContentValues getValuesTrailer(MovieTrailer trailer) {
        ContentValues values = new ContentValues();
        values.put(TrailersTable.COLUMN_ID_TRAILER, trailer.getId());
        values.put(TrailersTable.COLUMN_ID_MOVIE, trailer.getIdMovie());
        values.put(TrailersTable.COLUMN_KEY, trailer.getKey());
        values.put(TrailersTable.COLUMN_NAME, trailer.getName());
        return values;
    }

    public static ContentValues getValuesReview(MovieReview review) {
        ContentValues values = new ContentValues();
        values.put(ReviewsTable.COLUMN_ID_REVIEW, review.getId());
        values.put(ReviewsTable.COLUMN_ID_MOVIE, review.getIdMovie());
        values.put(ReviewsTable.COLUMN_AUTHOR, review.getAuthor());
        values.put(ReviewsTable.COLUMN_REVIEW, review.getReview());
        values.put(ReviewsTable.COLUMN_URL, review.getUrl());
        return values;
    }
}
