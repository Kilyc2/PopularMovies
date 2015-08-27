package com.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.popularmovies.R;
import com.popularmovies.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PosterAdapter extends ArrayAdapter<Movie> {

    public PosterAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.poster_item_movie, parent, false);
        }
        Movie movie = getItem(position);
        ImageView poster = (ImageView)convertView.findViewById(R.id.poster_item_movie);
        Picasso.with(getContext()).load(movie.getPosterW342()).into(poster);
        return poster;
    }
}
