package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;

public class Movie implements Parcelable {

    private long id;
    private String poster;
    private String title;
    private String date;
    private float rate;
    private String synopsis;
    private long duration;
    private boolean isFavorite;
    private ArrayList<MovieTrailer> trailers;
    private ArrayList<MovieReview> reviews;

    public Movie() {
        this.id = 0;
        this.poster = "";
        this.title = "";
        this.date = "";
        this.rate = 0f;
        this.synopsis = "";
        this.duration = 0;
        this.isFavorite = false;
        this.trailers = new ArrayList<MovieTrailer>();
        this.reviews = new ArrayList<MovieReview>();
    }

    public Movie(long id, String poster) {
        this.id = id;
        this.poster = poster;
        this.title = "";
        this.date = "";
        this.rate = 0f;
        this.synopsis = "";
        this.duration = 0;
        this.isFavorite = false;
        this.trailers = new ArrayList<MovieTrailer>();
        this.reviews = new ArrayList<MovieReview>();
    }

    public Movie(long id, String poster, String title, String date, float rate,
                 String synopsis, long duration) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.rate = rate;
        this.synopsis = TextUtils.equals(synopsis, "null") ? "" : synopsis;
        this.duration = duration;
        this.isFavorite = false;
        this.trailers = new ArrayList<MovieTrailer>();
        this.reviews = new ArrayList<MovieReview>();
    }

    public long getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getPosterW185() {
        return "http://image.tmdb.org/t/p/w185/" + poster;
    }

    public String getPosterW342() {
        return "http://image.tmdb.org/t/p/w342/" + poster;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDateDisplay() {
        try {
            return date.substring(0, 4);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public float getRate() {
        return rate;
    }

    public float getRateForRatingBar() {
        return rate * 0.5f;
    }

    public String getRateForDisplay() {
        return String.valueOf(rate) + "/10";
    }

    public String getSynopsis() {
        return synopsis;
    }

    public long getDuration() {
        return duration;
    }

    public String getDurationForDisplay() {
        return duration + " min";
    }

    public ArrayList<MovieTrailer> getTrailers() {
        return trailers;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite() {
        this.isFavorite = true;
    }

    public void setNoFavorite() {
        this.isFavorite = false;
    }

    public boolean hasTrailers() {
        return this.trailers.size() != 0;
    }

    public boolean hasReviews() {
        return this.reviews.size() != 0;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public void setVisibleReview(String idReview) {
        try {
            this.reviews.get(getIdReview(idReview)).setVisible();
        } catch (IndexOutOfBoundsException ioobe) {
            //NOTHING
        }
    }

    public void setInvisibleReview(String idReview) {
        try {
            this.reviews.get(getIdReview(idReview)).setInvisible();
        } catch (IndexOutOfBoundsException ioobe) {
            //NOTHING
        }
    }

    private int getIdReview(String idReview) {
        for (int i = 0; i < this.reviews.size(); i++) {
            if (TextUtils.equals(this.reviews.get(i).getId(), idReview)) {
                return i;
            }
        }
        return -1;
    }

    public Movie(Parcel in) {
        this.id = in.readLong();
        this.poster = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.rate = in.readFloat();
        this.synopsis = in.readString();
        this.duration = in.readLong();
        this.isFavorite = in.readInt() == 1;
        in.readTypedList(this.trailers, MovieTrailer.CREATOR);
        in.readTypedList(this.reviews, MovieReview.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.poster);
        out.writeString(this.title);
        out.writeString(this.date);
        out.writeFloat(this.rate);
        out.writeString(this.synopsis);
        out.writeLong(this.duration);
        out.writeInt(this.isFavorite ? 1 : 0);
        out.writeTypedList(this.trailers);
        out.writeTypedList(this.reviews);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
