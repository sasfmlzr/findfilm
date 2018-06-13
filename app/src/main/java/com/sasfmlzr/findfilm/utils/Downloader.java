package com.sasfmlzr.findfilm.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {
    private static final String TAG = "DownloadImage";

    public static Bitmap downloadImage(String urlImage) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(urlImage);
        Request requestMovie = builder.build();
        try {
            Response response = client.newCall(requestMovie).execute();
            return BitmapFactory.decodeStream(response.body().byteStream());
        } catch (Exception e) {
            Log.d(TAG, "error download " + urlImage);
            return null;
        }
    }
}
