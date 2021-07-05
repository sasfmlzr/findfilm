package com.sasfmlzr.findfilm

import com.sasfmlzr.findfilm.request.FilmStore
import com.sasfmlzr.findfilm.request.FindFilmComposeApi
import com.sasfmlzr.findfilm.request.FindFilmRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Graph {
    private const val BASE_URL = "https://api.themoviedb.org"

    val api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(FindFilmComposeApi::class.java)
    }

    val filmStore by lazy {
        FilmStore()
    }

    val findFilmRepository by lazy {
        FindFilmRepository(
            api = api,
            filmStore = filmStore,
            mainDispatcher = mainDispatcher
        )
    }

    private val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    private val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}