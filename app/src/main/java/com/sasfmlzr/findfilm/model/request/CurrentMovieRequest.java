package com.sasfmlzr.findfilm.model.request;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CurrentMovieRequest {
    private boolean adult;
    private String backdropPath;
    private Object belongsToCollection;
    private int budget;
    private List<Genre> genres = new ArrayList<>();
    private Object homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private List<ProductionCompany> productionCompanies = new ArrayList<>();
    private List<ProductionCountry> productionCountries = new ArrayList<>();
    private String releaseDate;
    private int revenue;
    private int runtime;
    private List<SpokenLanguage> spokenLanguages = new ArrayList<>();
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public CurrentMovieRequest(JSONObject currentMovieJSON) {
        try {
            this.adult = currentMovieJSON.getBoolean("adult");
            this.backdropPath = currentMovieJSON.getString("");
            this.belongsToCollection = null;
            this.budget = currentMovieJSON.getInt("budget");
            this.genres = null;
            this.homepage = null;
            this.id = currentMovieJSON.getInt("id");
            this.imdbId = currentMovieJSON.getString("imdb_id");
            this.originalLanguage = currentMovieJSON.getString("original_language");
            this.originalTitle = currentMovieJSON.getString("original_title");
            this.overview = currentMovieJSON.getString("overview");
            this.popularity = currentMovieJSON.getDouble("popularity");
            this.posterPath = currentMovieJSON.getString("poster_path");
            this.productionCompanies = null;
            this.productionCountries = null;
            this.releaseDate = currentMovieJSON.getString("release_date");
            this.revenue = currentMovieJSON.getInt("revenue");
            this.runtime = currentMovieJSON.getInt("runtime");
            this.spokenLanguages = null;
            this.status = currentMovieJSON.getString("status");
            this.tagline = currentMovieJSON.getString("tagline");
            this.title = currentMovieJSON.getString("title");
            this.video = currentMovieJSON.getBoolean("video");
            this.voteAverage = currentMovieJSON.getDouble("vote_average");
            this.voteCount = currentMovieJSON.getInt("vote_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static class Genre {
        private int id;
        private String name;

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class ProductionCompany {
        private Object logoPath;
        private String name;
        private String originCountry;

        public ProductionCompany(Object logoPath, String name, String originCountry) {
            this.logoPath = logoPath;
            this.name = name;
            this.originCountry = originCountry;
        }

        public Object getLogoPath() {
            return logoPath;
        }

        public String getName() {
            return name;
        }

        public String getOriginCountry() {
            return originCountry;
        }
    }

    private static class ProductionCountry {
        private String iso31661;
        private String name;

        public ProductionCountry(String iso31661, String name) {
            this.iso31661 = iso31661;
            this.name = name;
        }

        public String getIso31661() {
            return iso31661;
        }

        public String getName() {
            return name;
        }
    }

    private static class SpokenLanguage {
        private String iso6391;
        private String name;

        public SpokenLanguage(String iso6391, String name) {
            this.iso6391 = iso6391;
            this.name = name;
        }

        public String getIso6391() {
            return iso6391;
        }

        public String getName() {
            return name;
        }
    }
}
