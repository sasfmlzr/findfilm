package com.sasfmlzr.findfilm.request;

import com.google.gson.Gson;

public class JsonParserRequest {

    public CurrentMovieRequest currentMovieParce(String currentMovieJSON) {
        Gson gson = new Gson();
        return gson.fromJson(currentMovieJSON, CurrentMovieRequest.class);
    }

    public DiscoverMovieRequest discoverMovieParce(String discoverMovieJSON) {
        Gson gson = new Gson();
        return gson.fromJson(discoverMovieJSON, DiscoverMovieRequest.class);
    }

    public DiscoverMovieRequest searchMovieParce(String searchMovieJSON) {
        Gson gson = new Gson();
        return gson.fromJson(searchMovieJSON, DiscoverMovieRequest.class);
    }
}
