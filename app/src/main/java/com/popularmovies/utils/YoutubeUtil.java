package com.popularmovies.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class YoutubeUtil {

    private final static String YOUTUBE_BASE_URL = "http://www.youtube.com/watch";
    private final static String KEY_PARAM = "v";

    public static void playVideo(Context context, String keyVideo) {
        Uri uriYoutube = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, keyVideo)
                .build();
        context.startActivity(new Intent(Intent.ACTION_VIEW, uriYoutube));
    }
}
