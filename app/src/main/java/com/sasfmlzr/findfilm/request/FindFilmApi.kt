package com.sasfmlzr.findfilm.request

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FindFilmApi {
    @GET("https://api.themoviedb.org/3/discover/movie")
    fun getDiscoverMovie(
        @Query("api_key") API_KEY: String?,
        @Query("language") language: String?,
        @Query("page") page: Int
    ): Call<DiscoverMovieRequest>

    @GET("https://api.themoviedb.org/3/search/movie")
    fun getSearchMovie(
        @Query("api_key") API_KEY: String?,
        @Query("language") language: String?,
        @Query("query") movie: String?,
        @Query("page") page: Int
    ): Call<DiscoverMovieRequest>

    @GET("https://api.themoviedb.org/3/movie/{movie_id}")
    fun getCurrentMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") API_KEY: String?,
        @Query("language") language: String?
    ): Call<CurrentMovieRequest>
}