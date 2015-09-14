package com.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.popularmovies.Constants;
import com.popularmovies.R;
import com.popularmovies.fragments.MovieDetailsFragment;
import com.popularmovies.fragments.PopularMoviesFragment;

public class PopularMoviesActivity extends Activity
        implements PopularMoviesFragment.Callbacks {

    private boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        isDualPane = (findViewById(R.id.movie_details_container) != null);
    }

    @Override
    public void onItemSelected(long idMovie) {
        if (isDualPane) {
            Bundle arguments = new Bundle();
            arguments.putLong(Constants.ID_MOVIE, idMovie);
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, MovieDetailsActivity.class);
            detailIntent.putExtra(Constants.ID_MOVIE, idMovie);
            startActivity(detailIntent);
        }
    }

    public boolean isDualPane() {
        return isDualPane;
    }
}
