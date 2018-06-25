package com.sasfmlzr.findfilm.request;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;

public class RequestMovie {
    private static FindFilmApi findFilmApi;

    public RequestMovie() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        findFilmApi = retrofit.create(FindFilmApi.class);
    }

    public DiscoverMovieRequest discoverMovie(int page) {
        Response response;
        try {
            response = findFilmApi.getDiscoverMovie(API_KEY, LANGUAGE, page)
                    .execute();
            return (DiscoverMovieRequest) response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DiscoverMovieRequest searchMovie(String movie) {
        Response response;
        try {
            response = findFilmApi.getSearchMovie(API_KEY, LANGUAGE, movie, 1)
                    .execute();
            return (DiscoverMovieRequest) response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CurrentMovieRequest viewMovie(int movie_id) {
        Response response;
        try {
            response = findFilmApi.getCurrentMovie(movie_id, API_KEY, LANGUAGE)
                    .execute();
            return (CurrentMovieRequest) response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
