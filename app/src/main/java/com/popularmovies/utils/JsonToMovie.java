package com.popularmovies.utils;

import com.popularmovies.entities.Movie;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieTrailer;

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
    private final static String KEY = "key";
    private final static String NAME = "name";
    private final static String AUTHOR = "author";
    private final static String CONTENT = "content";
    private final static String URL = "url";

    public static ArrayList<Movie> getPopularMovies(String jsonData) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            JSONObject jsonMovies = new JSONObject(jsonData);
            JSONArray jsonListOfMovies = jsonMovies.getJSONArray(RESULTS);
            for (int i = 0; i < jsonListOfMovies.length(); i++) {
                JSONObject jsonMovie = jsonListOfMovies.getJSONObject(i);
                long id = jsonMovie.getLong(ID);
                String poster = jsonMovie.getString(POSTER);
                Movie movie = new Movie(id, poster);
                movies.add(movie);
            }
        } catch(JSONException json) {
            movies = new ArrayList<Movie>();
        }
        return movies;
    }

    public static Movie getMovieDetails(String jsonData) {
        try {
            JSONObject jsonMovie = new JSONObject(jsonData);
            long id = jsonMovie.getLong(ID);
            String title = jsonMovie.getString(TITLE);
            String poster = jsonMovie.getString(POSTER);
            String rate = jsonMovie.getString(VOTE_AVERAGE);
            String synopsis = jsonMovie.getString(SYNOPSIS);
            String releaseDate = jsonMovie.getString(RELEASE_DATE);
            long duration = jsonMovie.getLong(DURATION);
            Float rating = Float.valueOf(rate);
            return new Movie(id, poster, title, releaseDate, rating, synopsis, duration);
        } catch (JSONException jsonE) {
            return new Movie();
        }
    }

    public static ArrayList<MovieTrailer> getMovieTrailers(String jsonData) {
        ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
        try {
            JSONObject jsonTrailers = new JSONObject(jsonData);
            long idMovie = jsonTrailers.getLong(ID);
            JSONArray jsonListOfTrailers = jsonTrailers.getJSONArray(RESULTS);
            for (int i = 0; i < jsonListOfTrailers.length(); i++) {
                JSONObject jsonTrailer = jsonListOfTrailers.getJSONObject(i);
                String id = jsonTrailer.getString(ID);
                String key = jsonTrailer.getString(KEY);
                String name = jsonTrailer.getString(NAME);
                MovieTrailer trailer = new MovieTrailer(id, idMovie, key, name);
                movieTrailers.add(trailer);
            }
        } catch(JSONException json) {
            movieTrailers = new ArrayList<MovieTrailer>();
        }
        return movieTrailers;
    }

    public static ArrayList<MovieReview> getMovieReviews(String jsonData) {
        ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();
        try {
            JSONObject jsonReviews = new JSONObject(jsonData);
            long idMovie = jsonReviews.getLong(ID);
            JSONArray jsonListOfTrailers = jsonReviews.getJSONArray(RESULTS);
            for (int i = 0; i < jsonListOfTrailers.length(); i++) {
                JSONObject jsonTrailer = jsonListOfTrailers.getJSONObject(i);
                String id = jsonTrailer.getString(ID);
                String author = jsonTrailer.getString(AUTHOR);
                String content = jsonTrailer.getString(CONTENT);
                String url = jsonTrailer.getString(URL);
                MovieReview review = new MovieReview(id, idMovie, author, content, url);
                movieReviews.add(review);
            }
        } catch(JSONException json) {
            movieReviews = new ArrayList<MovieReview>();
        }
        return movieReviews;
    }
}
