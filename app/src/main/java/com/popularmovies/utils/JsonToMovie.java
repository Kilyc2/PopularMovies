package com.popularmovies.utils;

import com.popularmovies.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonToMovie {

    private final static String RESULTS = "results";
    private final static String ID = "id";
    private final static String POSTER = "poster_path";
    private final static String TITLE = "original_title";
    private final static String VOTE_AVERAGE = "vote_average";
    private final static String SYNOPSIS = "overview";
    private final static String RELEASE_DATE = "release_date";
    private final static String DURATION = "runtime";

    public static Movie getMovieDetails(String jsonData) {
        try {
            JSONObject jsonMovie = new JSONObject(jsonData);
            String id = jsonMovie.getString(ID);
            String title = jsonMovie.getString(TITLE);
            String poster = jsonMovie.getString(POSTER);
            String rate = jsonMovie.getString(VOTE_AVERAGE);
            String synopsis = jsonMovie.getString(SYNOPSIS);
            String releaseDate = jsonMovie.getString(RELEASE_DATE);
            String duration = jsonMovie.getString(DURATION);
            Float rating = Float.valueOf(rate);
            return new Movie(id, poster, title, releaseDate, rating, synopsis, duration, false);
        } catch (JSONException jsonE) {
            return new Movie();
        }
    }

    public static ArrayList<Movie> getPopularMovies(String jsonData) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            JSONObject jsonMovies = new JSONObject(jsonData);
            JSONArray jsonListOfMovies = jsonMovies.getJSONArray(RESULTS);
            for (int i = 0; i < jsonListOfMovies.length(); i++) {
                JSONObject jsonMovie = jsonListOfMovies.getJSONObject(i);
                String id = jsonMovie.getString(ID);
                String title = jsonMovie.getString(TITLE);
                String poster = jsonMovie.getString(POSTER);
                String rate = jsonMovie.getString(VOTE_AVERAGE);
                Float rating = Float.valueOf(rate);
                Movie movie = new Movie(id, poster, title, rating);
                movies.add(movie);
            }
        } catch(JSONException json) {
            movies = new ArrayList<Movie>();
        }
        return movies;
    }
}
