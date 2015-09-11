package com.popularmovies.data;


import android.database.sqlite.SQLiteDatabase;

import com.popularmovies.data.tables.MoviesTable;
import com.popularmovies.data.tables.ReviewsTable;
import com.popularmovies.data.tables.TrailersTable;

public class MoviesDbManager {

    public static void onCreateDatabase(SQLiteDatabase database) {
        MoviesTable.onCreate(database);
        TrailersTable.onCreate(database);
        ReviewsTable.onCreate(database);
    }

    public static void onUpgradeDatabase(SQLiteDatabase database) {
        MoviesTable.onUpgrade(database);
        TrailersTable.onUpgrade(database);
        ReviewsTable.onUpgrade(database);
        onCreateDatabase(database);
    }
}
