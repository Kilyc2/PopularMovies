package com.popularmovies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.popularmovies.Constants;

public class ReviewsTable implements BaseColumns {

    public static final String TABLE_NAME = "reviews";

    public static final String COLUMN_ID_REVIEW = "idReview";
    public static final String COLUMN_ID_MOVIE = "idMovie";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_REVIEW = "review";
    public static final String COLUMN_URL = "url";

    public static final String PATH_REVIEWS = "reviews";

    public static final Uri CONTENT_URI =
            Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(" +
            _ID + " integer primary key autoincrement, " +
            COLUMN_ID_REVIEW + " text not null, " +
            COLUMN_ID_MOVIE + " integer not null, " +
            COLUMN_AUTHOR + " text not null, " +
            COLUMN_REVIEW + " text not null, " +
            COLUMN_URL + " text not null " +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static Uri buildReviewUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
