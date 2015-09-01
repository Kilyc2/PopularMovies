package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.popularmovies.utils.Numeric;

public class Movie implements Parcelable {

    private String id;
    private String poster;
    private String title;
    private String date;
    private float rate;
    private String synopsis;
    private String duration;
    private boolean isFavorite;

    public Movie() {
        this.id = "";
        this.poster = "";
        this.title = "";
        this.date = "Unknown";
        this.rate = 0f;
        this.synopsis = "";
        this.duration = "0";
        this.isFavorite = false;
    }

    public Movie(String id, String poster, String title, String date, float rate, String synopsis,
                 String duration, boolean isFavorite) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.rate = rate;
        this.synopsis = synopsis.equals("null") ? "" : synopsis;
        this.duration = Numeric.isNumeric(duration) ? duration : "0";
        this.isFavorite = isFavorite;
    }

    public Movie(String id, String poster, String title, float rate) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.rate = rate;
    }

    public String getId() {
        return id;
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
        try {
            return date.substring(0, 4);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public float getRateForRatingBar() {
        return rate * 0.5f;
    }

    public String getRate() {
        return String.valueOf(rate) + "/10";
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getDuration() {
        return duration + " min";
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void markAsFavorite() {
        isFavorite = true;
    }

    public Movie(Parcel in) {
        this.id = in.readString();
        this.poster = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.rate = in.readFloat();
        this.synopsis = in.readString();
        this.duration = in.readString();
        this.isFavorite = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.poster);
        out.writeString(this.title);
        out.writeString(this.date);
        out.writeFloat(this.rate);
        out.writeString(this.synopsis);
        out.writeString(this.duration);
        out.writeInt(this.isFavorite ? 1 : 0);
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
