package com.sasfmlzr.findfilm.request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FindFilmApi {
    @GET("https://api.themoviedb.org/3/discover/movie")
    Call<DiscoverMovieRequest> getDiscoverMovie(@Query("api_key") String API_KEY,
                                                @Query("language") String language,
                                                @Query("page") int page);

    @GET("https://api.themoviedb.org/3/search/movie")
    Call<DiscoverMovieRequest> getSearchMovie(@Query("api_key") String API_KEY,
                                              @Query("language") String language,
                                              @Query("query") String movie,
                                              @Query("page") int page);

    @GET("https://api.themoviedb.org/3/movie/{movie_id}")
    Call<CurrentMovieRequest> getCurrentMovie(@Path("movie_id") int movie_id,
                                              @Query("api_key") String API_KEY,
                                              @Query("language") String language);
}
