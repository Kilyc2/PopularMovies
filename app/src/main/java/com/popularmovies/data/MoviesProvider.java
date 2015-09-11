package com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.popularmovies.Constants;
import com.popularmovies.data.tables.MoviesTable;
import com.popularmovies.data.tables.ReviewsTable;
import com.popularmovies.data.tables.TrailersTable;

public class MoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_ID = 101;
    public static final int TRAILER = 200;
    public static final int TRAILER_MOVIE_ID = 201;
    public static final int REVIEW = 300;
    public static final int REVIEW_MOVIE_ID = 301;

    private Cursor getFavoriteMovie(Uri uri) {
        SQLiteQueryBuilder favoriteMovieQueryBuilder = new SQLiteQueryBuilder();
        favoriteMovieQueryBuilder.setTables(MoviesTable.TABLE_NAME);
        String selectionMovie = MoviesTable._ID + " = ?";
        String _id = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{_id};

        return favoriteMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(), null,
                selectionMovie, selectionArgs, null, null, null);
    }

    private Cursor getTrailersOfFavoriteMovie(Uri uri) {
        SQLiteQueryBuilder favoriteMovieQueryBuilder = new SQLiteQueryBuilder();
        favoriteMovieQueryBuilder.setTables(TrailersTable.TABLE_NAME);
        String selectionMovie = TrailersTable.COLUMN_ID_MOVIE + " = ?";
        String idMovie = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{idMovie};

        return favoriteMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(), null,
                selectionMovie, selectionArgs, null, null, null);
    }

    private Cursor getReviewsOfFavoriteMovie(Uri uri) {
        SQLiteQueryBuilder favoriteMovieQueryBuilder = new SQLiteQueryBuilder();
        favoriteMovieQueryBuilder.setTables(ReviewsTable.TABLE_NAME);
        String selectionMovie = ReviewsTable.COLUMN_ID_MOVIE + " = ?";
        String idMovie = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{idMovie};

        return favoriteMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(), null,
                selectionMovie, selectionArgs, null, null, null);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Constants.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesTable.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MoviesTable.PATH_MOVIES + "/#", MOVIE_ID);
        matcher.addURI(authority, TrailersTable.PATH_TRAILERS, TRAILER);
        matcher.addURI(authority, TrailersTable.PATH_TRAILERS + "/#", TRAILER_MOVIE_ID);
        matcher.addURI(authority, ReviewsTable.PATH_REVIEWS, REVIEW);
        matcher.addURI(authority, ReviewsTable.PATH_REVIEWS + "/#", REVIEW_MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_ID:
                return MoviesTable.CONTENT_ITEM_TYPE;
            case TRAILER_MOVIE_ID:
                return TrailersTable.CONTENT_TYPE;
            case REVIEW_MOVIE_ID:
                return ReviewsTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID: {
                returnCursor = getFavoriteMovie(uri);
                break;
            }
            case TRAILER_MOVIE_ID: {
                returnCursor = getTrailersOfFavoriteMovie(uri);
                break;
            }
            case REVIEW_MOVIE_ID: {
                returnCursor = getReviewsOfFavoriteMovie(uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MoviesTable.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MoviesTable.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TRAILER: {
                long _id = db.insert(TrailersTable.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TrailersTable.buildTrailerUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW: {
                long _id = db.insert(ReviewsTable.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ReviewsTable.buildReviewUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String idMovie = uri.getLastPathSegment();
        String[] whereArgs = new String[]{idMovie};
        int rowsDeleted;
        switch (match) {
            case MOVIE_ID: {
                rowsDeleted = db.delete(MoviesTable.TABLE_NAME,
                        MoviesTable._ID + "= ?", whereArgs);
                break;
            }
            case TRAILER_MOVIE_ID: {
                rowsDeleted = db.delete(TrailersTable.TABLE_NAME,
                        TrailersTable.COLUMN_ID_MOVIE + "= ?", whereArgs);
                break;
            }
            case REVIEW_MOVIE_ID: {
                rowsDeleted = db.delete(ReviewsTable.TABLE_NAME,
                        ReviewsTable.COLUMN_ID_MOVIE + "= ?", whereArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
