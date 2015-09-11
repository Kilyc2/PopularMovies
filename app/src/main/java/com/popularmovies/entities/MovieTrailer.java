package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable {

    private String id;
    private long idMovie;
    private String key;
    private String name;

    public MovieTrailer(String id, long idMovie, String key, String name) {
        this.id = id;
        this.idMovie = idMovie;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public long getIdMovie() {
        return idMovie;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public MovieTrailer(Parcel in) {
        this.id = in.readString();
        this.idMovie = in.readLong();
        this.key = in.readString();
        this.name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeLong(this.idMovie);
        out.writeString(this.key);
        out.writeString(this.name);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR =
            new Parcelable.Creator<MovieTrailer>() {

                public MovieTrailer createFromParcel(Parcel in) {
                    return new MovieTrailer(in);
                }

                public MovieTrailer[] newArray(int size) {
                    return new MovieTrailer[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }
}
