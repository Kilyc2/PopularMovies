package com.popularmovies.data.tables;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.popularmovies.Constants;

public class TrailersTable implements BaseColumns {

    public static final String TABLE_NAME = "trailers";

    public static final String COLUMN_ID_TRAILER = "idTrailer";
    public static final String COLUMN_ID_MOVIE = "idMovie";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_NAME = "name";

    public static final String PATH_TRAILERS = "trailers";

    public static final Uri CONTENT_URI =
            Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
            Constants.CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(" +
            _ID + " integer primary key autoincrement, " +
            COLUMN_ID_TRAILER + " text not null, " +
            COLUMN_ID_MOVIE + " integer not null, " +
            COLUMN_KEY + " text not null, " +
            COLUMN_NAME + " text not null " +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static Uri buildTrailerUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
