package com.popularmovies.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.popularmovies.Constants;
import com.popularmovies.R;
import com.popularmovies.activities.MovieDetailsActivity;
import com.popularmovies.activities.SettingsActivity;
import com.popularmovies.adapters.PosterAdapter;
import com.popularmovies.entities.Movie;
import com.popularmovies.utils.JsonManagerData;
import com.popularmovies.utils.JsonToMovie;

import java.util.ArrayList;

public class PopularMoviesFragment extends Fragment {

    private final static String KEY_STATE_MOVIES = "key_movies";

    private PosterAdapter posterAdapter;
    private ArrayList<Movie> popularMovies;
    private ProgressBar progressBarMovies;

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        posterAdapter = new PosterAdapter(getActivity(), new ArrayList<Movie>());
        GridView movie = (GridView)rootView.findViewById(R.id.grid_popular_movies);
        progressBarMovies = (ProgressBar)rootView.findViewById(R.id.progress_bar_movies);
        movie.setAdapter(posterAdapter);
        movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long idMovie = posterAdapter.getItem(position).getId();
                Intent detailMovieIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                detailMovieIntent.putExtra(Constants.ID_MOVIE, idMovie);
                startActivity(detailMovieIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_MOVIES)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = sharedPref.getString(Constants.KEY_PREF_SORT_ORDER, "");
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute(sortOrder);
            SharedPreferences.Editor editorSharedPref = sharedPref.edit();
            editorSharedPref.putBoolean(Constants.KEY_PREF_ORDER_CHANGED, false);
            editorSharedPref.apply();
        } else {
            popularMovies = savedInstanceState.getParcelableArrayList(KEY_STATE_MOVIES);
            setPosterAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getBoolean(Constants.KEY_PREF_ORDER_CHANGED, false)) {
            String sortOrder = sharedPref.getString(Constants.KEY_PREF_SORT_ORDER, "");
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute(sortOrder);
            SharedPreferences.Editor editorSharedPref = sharedPref.edit();
            editorSharedPref.putBoolean(Constants.KEY_PREF_ORDER_CHANGED, false);
            editorSharedPref.apply();
        }
    }

    private void setPosterAdapter() {
        posterAdapter.clear();
        posterAdapter.addAll(popularMovies);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_STATE_MOVIES, popularMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_popular_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMovies.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY)
                    .build();
            String moviesJsonStr = JsonManagerData.getJsonData(builtUri);
            return JsonToMovie.getPopularMovies(moviesJsonStr);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            popularMovies = movies;
            setPosterAdapter();
            progressBarMovies.setVisibility(View.GONE);
        }
    }
}
