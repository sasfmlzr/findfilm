package com.sasfmlzr.findfilm.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    private static final String TAG = "DownloadImage";

    public static Bitmap downloadImage(String urlImage) {
        try {
            URL url = new URL(urlImage);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            Log.d(TAG, "error download " + urlImage);
            return null;
        }
    }
}
