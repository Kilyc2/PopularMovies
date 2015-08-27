package com.popularmovies.activities;

import android.app.Activity;
import android.os.Bundle;

import com.popularmovies.R;
import com.popularmovies.fragments.MovieDetailsFragment;

public class MovieDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            MovieDetailsFragment movieDetails = new MovieDetailsFragment();
            movieDetails.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.container, movieDetails)
                    .commit();
        }
    }
}