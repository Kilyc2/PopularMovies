package com.popularmovies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.popularmovies.Constants;

public class MoviesTable implements BaseColumns {

    public static final String TABLE_NAME = "movies";

    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_DURATION = "duration";

    public static final String PATH_MOVIES = "movies";

    public static final Uri CONTENT_URI =
            Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIES;

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(" +
            _ID + " integer primary key, " +
            COLUMN_POSTER + " text not null, " +
            COLUMN_TITLE + " text not null, " +
            COLUMN_DATE + " text not null, " +
            COLUMN_RATE + " real not null, " +
            COLUMN_SYNOPSIS + " text not null, " +
            COLUMN_DURATION + " integer not null " +
        ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
