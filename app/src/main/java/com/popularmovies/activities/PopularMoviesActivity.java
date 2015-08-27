package com.popularmovies.activities;

import android.app.Activity;
import android.os.Bundle;

import com.popularmovies.R;
import com.popularmovies.fragments.PopularMoviesFragment;

public class PopularMoviesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PopularMoviesFragment())
                    .commit();
        }
    }
}
