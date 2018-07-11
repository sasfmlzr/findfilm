package com.sasfmlzr.findfilm.model;

import com.sasfmlzr.findfilm.request.FindFilmApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private final static String BASE_URL = "https://api.themoviedb.org";
    private static FindFilmApi findFilmApi;

    private RetrofitSingleton() {
    }

    public static FindFilmApi getFindFilmApi() {
        if (findFilmApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            findFilmApi = retrofit.create(FindFilmApi.class);
        }
        return findFilmApi;
    }
}
