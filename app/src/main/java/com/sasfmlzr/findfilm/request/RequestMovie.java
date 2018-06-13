package com.sasfmlzr.findfilm.request;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;

public class RequestMovie {

    private String connection(String link) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(link);
        Request requestMovie = builder.build();
        try {
            Response response = client.newCall(requestMovie).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String discoverMovie(int page) {
        return connection("https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "" +
                "&language=" + LANGUAGE + "&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + page);
    }

    public String searchMovie(String movie) {
        return connection("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY +
                "&language=" + LANGUAGE + "&query=" + movie + "&page=1&include_adult=false");
    }

    public String viewMovie(int movie_id) {
        return connection("https://api.themoviedb.org/3/movie/" + movie_id +
                "?api_key=" + API_KEY + "&language=" + LANGUAGE);
    }
}
