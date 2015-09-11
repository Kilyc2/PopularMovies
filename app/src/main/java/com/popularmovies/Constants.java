package com.popularmovies;

import android.net.Uri;

public class Constants {

    public final static String API_KEY = "xxx";
    public final static String ID_MOVIE = "ID";
    public final static String KEY_PREF_SORT_ORDER = "pref_sort_movies_list";
    public final static String KEY_PREF_ORDER_CHANGED = "pref_order_changed";
    public static final String CONTENT_AUTHORITY = "com.popularmovies.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
