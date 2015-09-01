package com.popularmovies.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.popularmovies.Constants;
import com.popularmovies.R;
import com.popularmovies.entities.Movie;
import com.popularmovies.utils.JsonManagerData;
import com.popularmovies.utils.JsonToMovie;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {

    private final static String KEY_STATE_MOVIE = "key_movie";

    private View movieView;
    private Movie movieDetails;
    private ProgressBar progressBarMovie;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        progressBarMovie = (ProgressBar)movieView.findViewById(R.id.progress_bar_movies);
        return movieView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_MOVIE)) {
            FetchMovieDetailsTask movieDetailsTask = new FetchMovieDetailsTask();
            String idMovie = getArguments().getString(Constants.ID_MOVIE);
            movieDetailsTask.execute(idMovie);
        } else {
            movieDetails = savedInstanceState.getParcelable(KEY_STATE_MOVIE);
            setMovieView();
        }
    }

    private void setMovieView() {
        TextView title = (TextView)movieView.findViewById(R.id.title_movie_detail);
        title.setText(movieDetails.getTitle());
        ImageView poster = (ImageView)movieView.findViewById(R.id.poster_movie_detail);
        Picasso.with(getActivity()).load(movieDetails.getPosterW342()).into(poster);
        TextView releaseDate = (TextView)movieView.findViewById(R.id.date_movie_detail);
        releaseDate.setText(movieDetails.getDate());
        TextView duration = (TextView)movieView.findViewById(R.id.duration_movie_detail);
        duration.setText(movieDetails.getDuration());
        TextView numericRating = (TextView)movieView.findViewById(R.id.numeric_rating_movie_detail);
        numericRating.setText(movieDetails.getRate());
        RatingBar rate = (RatingBar)movieView.findViewById(R.id.rating_movie_detail);
        rate.setRating(movieDetails.getRateForRatingBar());
        rate.setVisibility(View.VISIBLE);
        TextView synopsis = (TextView)movieView.findViewById(R.id.synopsis_movie_detail);
        synopsis.setText(movieDetails.getSynopsis());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STATE_MOVIE, movieDetails);
        super.onSaveInstanceState(outState);
    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMovie.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(String... params) {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0];
            final String API_KEY_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY)
                    .build();
            String jsonMovieDetail = JsonManagerData.getJsonData(builtUri);
            return JsonToMovie.getMovieDetails(jsonMovieDetail);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            movieDetails = movie;
            setMovieView();
            progressBarMovie.setVisibility(View.GONE);
        }
    }
}
