package com.sasfmlzr.findfilm.model.request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CurrentMovieRequest {
    private boolean adult;
    private String backdropPath;
    private BelongsToCollection belongsToCollection;
    private int budget;
    private List<Genre> genres = new ArrayList<>();
    private String homepage;
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
            this.backdropPath = currentMovieJSON.getString("backdrop_path");
            this.budget = currentMovieJSON.getInt("budget");
            this.homepage = currentMovieJSON.getString("homepage");
            this.id = currentMovieJSON.getInt("id");
            this.imdbId = currentMovieJSON.getString("imdb_id");
            this.originalLanguage = currentMovieJSON.getString("original_language");
            this.originalTitle = currentMovieJSON.getString("original_title");
            this.overview = currentMovieJSON.getString("overview");
            this.popularity = currentMovieJSON.getDouble("popularity");
            this.posterPath = currentMovieJSON.getString("poster_path");
            this.releaseDate = currentMovieJSON.getString("release_date");
            this.revenue = currentMovieJSON.getInt("revenue");
            this.runtime = currentMovieJSON.getInt("runtime");
            this.status = currentMovieJSON.getString("status");
            this.tagline = currentMovieJSON.getString("tagline");
            this.title = currentMovieJSON.getString("title");
            this.video = currentMovieJSON.getBoolean("video");
            this.voteAverage = currentMovieJSON.getDouble("vote_average");
            this.voteCount = currentMovieJSON.getInt("vote_count");

            JSONObject belongsToCollectionArray = currentMovieJSON.getJSONObject("belongs_to_collection");

            belongsToCollection = new BelongsToCollection(
                    belongsToCollectionArray.getInt("id"),
                    belongsToCollectionArray.getString("name"),
                    belongsToCollectionArray.getString("poster_path"),
                    belongsToCollectionArray.getString("backdrop_path"));

            JSONArray genresArray = currentMovieJSON.getJSONArray("genres");
            for (int countArray = 0; countArray < genresArray.length(); countArray++) {
                JSONObject currentObject = genresArray.getJSONObject(countArray);
                genres.add(new Genre(
                        currentObject.getInt("id"),
                        currentObject.getString("name")));
            }

            JSONArray productionCompaniesArray = currentMovieJSON.getJSONArray("production_companies");
            for (int countArray = 0; countArray < productionCompaniesArray.length(); countArray++) {
                JSONObject currentObject = productionCompaniesArray.getJSONObject(countArray);
                productionCompanies.add(new ProductionCompany(
                        currentObject.getInt("id"),
                        currentObject.getString("name"),
                        currentObject.getString("logo_path"),
                        currentObject.getString("origin_country")));
            }

            JSONArray productionCountriesArray = currentMovieJSON.getJSONArray("production_countries");
            for (int countArray = 0; countArray < productionCountriesArray.length(); countArray++) {
                JSONObject currentObject = productionCountriesArray.getJSONObject(countArray);
                productionCountries.add(new ProductionCountry(
                        currentObject.getString("iso_3166_1"),
                        currentObject.getString("name")));
            }

            JSONArray spokenLanguagesArray = currentMovieJSON.getJSONArray("spoken_languages");
            for (int countArray = 0; countArray < spokenLanguagesArray.length(); countArray++) {
                JSONObject currentObject = spokenLanguagesArray.getJSONObject(countArray);
                spokenLanguages.add(new SpokenLanguage(
                        currentObject.getString("iso_639_1"),
                        currentObject.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class BelongsToCollection {
        private int id;
        private String name;
        private String posterPath;
        private String backdropPath;

        public BelongsToCollection(int id, String name, String posterPath, String backdropPath) {
            this.id = id;
            this.name = name;
            this.posterPath = posterPath;
            this.backdropPath = backdropPath;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public String getBackdropPath() {
            return backdropPath;
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
        private int id;
        private Object logoPath;
        private String name;
        private String originCountry;

        public ProductionCompany(int id, Object logoPath, String name, String originCountry) {
            this.id = id;
            this.logoPath = logoPath;
            this.name = name;
            this.originCountry = originCountry;
        }

        public int getId() {
            return id;
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
