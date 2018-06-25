package com.sasfmlzr.findfilm.model;

import com.sasfmlzr.findfilm.request.FindFilmApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static FindFilmApi findFilmApi;

    private RetrofitSingleton() {
    }

    public static FindFilmApi getFindFilmApi() {
        if (findFilmApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            findFilmApi = retrofit.create(FindFilmApi.class);
        }
        return findFilmApi;
    }
}
