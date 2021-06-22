package com.sasfmlzr.findfilm.model

import com.sasfmlzr.findfilm.request.FindFilmApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    private const val BASE_URL = "https://api.themoviedb.org"
    @JvmStatic
    var findFilmApi: FindFilmApi? = null
        get() {
            if (field == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                field = retrofit.create(FindFilmApi::class.java)
            }
            return field
        }
        private set
}