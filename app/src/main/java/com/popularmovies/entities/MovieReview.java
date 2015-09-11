package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    private String id;
    private long idMovie;
    private String author;
    private String review;
    private String url;
    private boolean isVisible;


    public MovieReview(String id, long idMovie, String author, String review, String url) {
        this.id = id;
        this.idMovie = idMovie;
        this.author = author;
        this.review = review;
        this.url = url;
        this.isVisible = false;
    }

    public MovieReview(Parcel in) {
        this.id = in.readString();
        this.idMovie = in.readLong();
        this.author = in.readString();
        this.review = in.readString();
        this.url = in.readString();
        this.isVisible = in.readInt() == 1;
    }

    public String getId() {
        return id;
    }

    public long getIdMovie() {
        return idMovie;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    public String getUrl() {
        return url;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible() {
        this.isVisible = true;
    }

    public void setInvisible() {
        this.isVisible = false;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeLong(this.idMovie);
        out.writeString(this.author);
        out.writeString(this.review);
        out.writeString(this.url);
        out.writeInt(this.isVisible ? 1 : 0);
    }

    public static final Parcelable.Creator<MovieReview> CREATOR =
            new Parcelable.Creator<MovieReview>() {

                public MovieReview createFromParcel(Parcel in) {
                    return new MovieReview(in);
                }

                public MovieReview[] newArray(int size) {
                    return new MovieReview[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }
}
