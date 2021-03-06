package com.popularmovies.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import com.popularmovies.activities.SettingsActivity;
import com.popularmovies.adapters.PosterAdapter;
import com.popularmovies.data.tables.MoviesTable;
import com.popularmovies.entities.Movie;
import com.popularmovies.utils.CursorUtil;
import com.popularmovies.utils.JsonManagerData;
import com.popularmovies.utils.JsonToMovie;

import java.util.ArrayList;

public class PopularMoviesFragment extends Fragment {

    private final static String KEY_STATE_MOVIES = "key_movies";

    private PosterAdapter posterAdapter;
    private ArrayList<Movie> popularMovies;
    private ProgressBar progressBarMovies;
    private Callbacks mCallbacks = movieCallbacks;

    public PopularMoviesFragment() {
    }

    public interface Callbacks {
        public void onItemSelected(long id);
    }

    private static Callbacks movieCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }
    };

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
        GridView listPopularMovies = (GridView)rootView.findViewById(R.id.grid_popular_movies);
        progressBarMovies = (ProgressBar)rootView.findViewById(R.id.progress_bar_movies);
        listPopularMovies.setAdapter(posterAdapter);
        listPopularMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallbacks.onItemSelected(posterAdapter.getItem(position).getId());
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = movieCallbacks;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_MOVIES)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = sharedPref.getString(Constants.KEY_PREF_SORT_ORDER, "");
            if (isSortOrderByFavorites(sortOrder)) {
                FetchFavoritesMoviesTask favoritesMoviesTask = new FetchFavoritesMoviesTask();
                favoritesMoviesTask.execute();
            } else {
                FetchMoviesTask moviesTask = new FetchMoviesTask();
                moviesTask.execute(sortOrder);
            }
        } else {
            popularMovies = savedInstanceState.getParcelableArrayList(KEY_STATE_MOVIES);
            setPosterAdapter();
        }
    }

    private boolean isSortOrderByFavorites(String sortOrder){
        return TextUtils.equals(sortOrder, getString(R.string.pref_sort_title_favorites));
    }

    private void setPosterAdapter() {
        posterAdapter.clear();
        posterAdapter.addAll(popularMovies);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATE_MOVIES, popularMovies);
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

    public class FetchFavoritesMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMovies.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(MoviesTable.CONTENT_URI,
                    null, null, null, null);
            ArrayList<Movie> favoritesMovies = new ArrayList<Movie>();
            if(CursorUtil.isValidCursor(cursor)) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(MoviesTable._ID));
                    String poster = cursor.getString(
                            cursor.getColumnIndex(MoviesTable.COLUMN_POSTER));
                    Movie movie = new Movie(id, poster);
                    favoritesMovies.add(movie);
                } while (cursor.moveToNext());
            }
            CursorUtil.closeCursor(cursor);
            return favoritesMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            popularMovies = movies;
            setPosterAdapter();
            progressBarMovies.setVisibility(View.GONE);
        }
    }
}
