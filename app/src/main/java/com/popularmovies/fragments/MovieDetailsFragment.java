package com.popularmovies.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.popularmovies.Constants;
import com.popularmovies.R;
import com.popularmovies.data.tables.MoviesTable;
import com.popularmovies.data.tables.ReviewsTable;
import com.popularmovies.data.tables.TrailersTable;
import com.popularmovies.entities.Movie;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieTrailer;
import com.popularmovies.utils.ContentValuesUtil;
import com.popularmovies.utils.CursorUtil;
import com.popularmovies.utils.JsonManagerData;
import com.popularmovies.utils.JsonToMovie;
import com.popularmovies.utils.YoutubeUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsFragment extends Fragment {

    private final static String KEY_STATE_MOVIE = "key_movie";

    private View movieView;
    private Movie movieDetails;
    private ProgressBar progressBarMovie;
    private ContentResolver contentResolver;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentResolver = getActivity().getContentResolver();
        movieView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        progressBarMovie = (ProgressBar)movieView.findViewById(R.id.progress_bar_movies);
        return movieView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_STATE_MOVIE)) {
            FetchFavoriteMovieTask favoriteMovieDetailsTask = new FetchFavoriteMovieTask();
            long idMovie = getArguments().getLong(Constants.ID_MOVIE);
            favoriteMovieDetailsTask.execute(idMovie);
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
        releaseDate.setText(movieDetails.getDateDisplay());
        TextView duration = (TextView)movieView.findViewById(R.id.duration_movie_detail);
        duration.setText(movieDetails.getDurationForDisplay());
        TextView numericRating = (TextView)movieView.findViewById(R.id.numeric_rating_movie_detail);
        numericRating.setText(movieDetails.getRateForDisplay());
        RatingBar rate = (RatingBar)movieView.findViewById(R.id.rating_movie_detail);
        rate.setRating(movieDetails.getRateForRatingBar());
        rate.setVisibility(View.VISIBLE);
        TextView synopsis = (TextView)movieView.findViewById(R.id.synopsis_movie_detail);
        synopsis.setText(movieDetails.getSynopsis());
        ImageButton favoriteButton = (ImageButton)movieView.findViewById(R.id.button_favorite);
        favoriteButton.setVisibility(View.VISIBLE);
        if (movieDetails.isFavorite()) {
            setButtonMarkAsFavorite(favoriteButton);
        } else {
            setButtonMarkAsNoFavorite(favoriteButton);
        }
        favoriteButton.setTag(movieDetails.isFavorite());
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = (Boolean)view.getTag();
                if (isFavorite) {
                    setButtonMarkAsNoFavorite((ImageButton) view);
                    movieDetails.setNoFavorite();
                    quitMovieAsFavorite(movieDetails.getId());
                } else {
                    setButtonMarkAsFavorite((ImageButton) view);
                    movieDetails.setFavorite();
                    saveMovieAsFavorite(movieDetails);
                }
                view.setTag(movieDetails.isFavorite());
            }
        });
        if (movieDetails.hasTrailers()) {
            setTrailers(movieDetails.getTrailers());
        }
        if (movieDetails.hasReviews()) {
            setReviews(movieDetails.getReviews());
        }
    }

    private void setTrailers(ArrayList<MovieTrailer> movieTrailers) {
        LinearLayout blockTrailers = (LinearLayout)movieView.findViewById(R.id.movie_trailers);
        blockTrailers.setVisibility(View.VISIBLE);
        LinearLayout trailers = (LinearLayout)blockTrailers.findViewById(R.id.trailer_list);
        for (MovieTrailer trailer : movieTrailers) {
            LinearLayout trailerItem = (LinearLayout)LayoutInflater.from(getActivity())
                    .inflate(R.layout.trailer_item, trailers, false);
            TextView trailerName = (TextView)trailerItem.findViewById(R.id.name_trailer);
            trailerName.setText(trailer.getName());
            trailerName.setTag(trailer.getKey());
            trailerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View trailer) {
                    String keyTrailer = trailer.getTag().toString();
                    YoutubeUtil.playVideo(getActivity(), keyTrailer);
                }
            });
            trailers.addView(trailerItem);
        }
    }

    private void setReviews(ArrayList<MovieReview> movieReviews) {
        LinearLayout blockReviews = (LinearLayout)movieView.findViewById(R.id.movie_reviews);
        blockReviews.setVisibility(View.VISIBLE);
        LinearLayout reviews = (LinearLayout)blockReviews.findViewById(R.id.review_list);
        for (MovieReview review : movieReviews) {
            LinearLayout reviewItem = (LinearLayout)LayoutInflater.from(getActivity())
                    .inflate(R.layout.review_item, reviews, false);
            TextView reviewAuthor = (TextView)reviewItem.findViewById(R.id.review_author);
            reviewAuthor.setText("A movie review by " + review.getAuthor());
            TextView reviewText = (TextView)reviewItem.findViewById(R.id.review_text);
            reviewText.setText(review.getReview());
            reviewText.setTag(review.getId());
            if (review.isVisible()) {
                setTextViewReviewVisible(reviewAuthor, reviewText);
            } else {
                setTextViewReviewInvisible(reviewAuthor, reviewText);
            }
            reviewAuthor.setTag(reviewText);
            reviews.addView(reviewItem);
            reviewAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView reviewAuthor = (TextView)view;
                    TextView reviewText = (TextView)reviewAuthor.getTag();
                    if (reviewText.getVisibility() == View.VISIBLE) {
                        setTextViewReviewInvisible(reviewAuthor, reviewText);
                    } else {
                        setTextViewReviewVisible(reviewAuthor, reviewText);
                    }
                }
            });
        }
    }

    private void setTextViewReviewInvisible(TextView reviewAuthor, TextView reviewText) {
        setReviewAuthorDrawable(reviewAuthor, R.drawable.ic_expand_more_black_24dp);
        reviewText.animate().alpha(0).start();
        reviewText.setVisibility(View.GONE);
        String idReview = reviewText.getTag().toString();
        movieDetails.setInvisibleReview(idReview);
    }

    private void setTextViewReviewVisible(TextView reviewAuthor, TextView reviewText) {
        setReviewAuthorDrawable(reviewAuthor, R.drawable.ic_expand_less_black_24dp);
        reviewText.animate().alpha(1).start();
        reviewText.setVisibility(View.VISIBLE);
        String idReview = reviewText.getTag().toString();
        movieDetails.setVisibleReview(idReview);
    }

    @TargetApi(17)
    private void setReviewAuthorDrawable(TextView reviewAuthor, int drawable) {
        if (isSDKOver16()) {
            reviewAuthor.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0);
        } else {
            reviewAuthor.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
        }
    }

    private boolean isSDKOver16(){
        return android.os.Build.VERSION.SDK_INT > 16;
    }

    private void setButtonMarkAsFavorite(ImageButton buttonFavorite) {
        buttonFavorite.setImageResource(R.drawable.ic_favorite_black_48dp);
    }

    private void setButtonMarkAsNoFavorite(ImageButton buttonFavorite) {
        buttonFavorite.setImageResource(R.drawable.ic_favorite_border_black_48dp);
    }

    private void saveMovieAsFavorite(Movie movie) {
        contentResolver.insert(MoviesTable.CONTENT_URI,
                ContentValuesUtil.getValuesFavoriteMovie(movie));
        insertTrailersFromFavoriteMovie(movie);
        insertReviewsFromFavoriteMovie(movie);
    }

    private void insertTrailersFromFavoriteMovie(Movie favoriteMovie) {
        for (MovieTrailer trailer : favoriteMovie.getTrailers()) {
            contentResolver.insert(TrailersTable.CONTENT_URI,
                    ContentValuesUtil.getValuesTrailer(trailer));
        }
    }

    private void insertReviewsFromFavoriteMovie(Movie favoriteMovie) {
        for (MovieReview review : favoriteMovie.getReviews()) {
            contentResolver.insert(ReviewsTable.CONTENT_URI,
                    ContentValuesUtil.getValuesReview(review));
        }
    }

    private void quitMovieAsFavorite(long idMovie) {
        contentResolver.delete(MoviesTable.buildMovieUri(idMovie), null, null);
        contentResolver.delete(TrailersTable.buildTrailerUri(idMovie), null, null);
        contentResolver.delete(ReviewsTable.buildReviewUri(idMovie), null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_STATE_MOVIE, movieDetails);
        super.onSaveInstanceState(outState);
    }

    public class FetchMovieDetailsTask extends AsyncTask<Long, Void, Movie> {

        @Override
        protected Movie doInBackground(Long... params) {
            long idMovie = params[0];
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + idMovie;
            final String API_KEY_PARAM = "api_key";
            Uri uriMovieDetails = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY)
                    .build();
            String jsonMovieDetail = JsonManagerData.getJsonData(uriMovieDetails);
            Movie movieDetail = JsonToMovie.getMovieDetails(jsonMovieDetail);
            final String MOVIES_VIDEOS_URL = MOVIES_BASE_URL + "/videos";
            Uri uriMovieTrailers = Uri.parse(MOVIES_VIDEOS_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY)
                    .build();
            String jsonMovieTrailers = JsonManagerData.getJsonData(uriMovieTrailers);
            movieDetail.setTrailers(JsonToMovie.getMovieTrailers(jsonMovieTrailers));
            final String MOVIES_REVIEWS_URL = MOVIES_BASE_URL + "/reviews";
            Uri uriMovieReviews = Uri.parse(MOVIES_REVIEWS_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, Constants.API_KEY)
                    .build();
            String jsonMovieReviews = JsonManagerData.getJsonData(uriMovieReviews);
            movieDetail.setReviews(JsonToMovie.getMovieReviews(jsonMovieReviews));
            return movieDetail;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            movieDetails = movie;
            setMovieView();
            progressBarMovie.setVisibility(View.GONE);
        }
    }

    public class FetchFavoriteMovieTask extends AsyncTask<Long, Void, Movie> {

        long idMovie;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMovie.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(Long... params) {
            idMovie = params[0];
            Movie favoriteMovie = getMovieFromDB(idMovie);
            if (favoriteMovie == null) {
                return null;
            }
            favoriteMovie.setTrailers(getTrailersMovieFromDB(idMovie));
            favoriteMovie.setReviews(getReviewsMovieFromDB(idMovie));
            return favoriteMovie;
        }

        private Movie getMovieFromDB(long idMovie) {
            Cursor cursor = contentResolver.query(MoviesTable.buildMovieUri(idMovie),
                    null, null, null, null);
            Movie favoriteMovie = null;
            if (CursorUtil.isValidCursor(cursor)) {
                long id = cursor.getLong(cursor.getColumnIndex(MoviesTable._ID));
                String title = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_TITLE));
                String poster = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_POSTER));
                String rate = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_RATE));
                String synopsis = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_SYNOPSIS));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_DATE));
                long duration = cursor.getLong(cursor.getColumnIndex(MoviesTable.COLUMN_DURATION));
                Float rating = Float.valueOf(rate);
                favoriteMovie = new Movie(id, poster, title, releaseDate, rating, synopsis,
                        duration);
                favoriteMovie.setFavorite();
            }
            CursorUtil.closeCursor(cursor);
            return favoriteMovie;
        }

        private ArrayList<MovieTrailer> getTrailersMovieFromDB(long idMovie) {
            ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
            Cursor cursor = contentResolver.query(TrailersTable.buildTrailerUri(idMovie),
                    null, null, null, null);
            if (CursorUtil.isValidCursor(cursor)) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(TrailersTable.COLUMN_ID_TRAILER));
                    String key = cursor.getString(cursor.getColumnIndex(TrailersTable.COLUMN_KEY));
                    String name = cursor.getString(cursor.getColumnIndex(TrailersTable.COLUMN_NAME));
                    MovieTrailer trailer = new MovieTrailer(id, idMovie, key, name);
                    movieTrailers.add(trailer);
                } while(cursor.moveToNext());
            }
            CursorUtil.closeCursor(cursor);
            return movieTrailers;
        }

        private ArrayList<MovieReview> getReviewsMovieFromDB(long idMovie) {
            ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();
            Cursor cursor = contentResolver.query(ReviewsTable.buildReviewUri(idMovie),
                    null, null, null, null);
            if (CursorUtil.isValidCursor(cursor)) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(ReviewsTable.COLUMN_ID_REVIEW));
                    String author = cursor.getString(cursor.getColumnIndex(ReviewsTable.COLUMN_AUTHOR));
                    String content = cursor.getString(cursor.getColumnIndex(ReviewsTable.COLUMN_REVIEW));
                    String url = cursor.getString(cursor.getColumnIndex(ReviewsTable.COLUMN_URL));
                    MovieReview review = new MovieReview(id, idMovie, author, content, url);
                    movieReviews.add(review);
                } while(cursor.moveToNext());
            }
            CursorUtil.closeCursor(cursor);
            return movieReviews;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if (movie == null) {
                FetchMovieDetailsTask movieDetailsTask = new FetchMovieDetailsTask();
                movieDetailsTask.execute(idMovie);
            } else {
                movieDetails = movie;
                setMovieView();
                progressBarMovie.setVisibility(View.GONE);
            }
        }
    }
}
