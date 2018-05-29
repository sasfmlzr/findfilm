package com.sasfmlzr.findfilm.model.request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParserRequest {

    public CurrentMovieRequest currentMovieParce(JSONObject currentMovieJSON) {
        boolean adult;
        String backdropPath;
        CurrentMovieRequest.BelongsToCollection belongsToCollection;
        int budget;
        List<CurrentMovieRequest.Genre> genres = new ArrayList<>();
        String homepage;
        int id;
        String imdbId;
        String originalLanguage;
        String originalTitle;
        String overview;
        double popularity;
        String posterPath;
        List<CurrentMovieRequest.ProductionCompany> productionCompanies = new ArrayList<>();
        List<CurrentMovieRequest.ProductionCountry> productionCountries = new ArrayList<>();
        String releaseDate;
        int revenue;
        int runtime;
        List<CurrentMovieRequest.SpokenLanguage> spokenLanguages = new ArrayList<>();
        String status;
        String tagline;
        String title;
        boolean video;
        double voteAverage;
        int voteCount;

        try {
            adult = currentMovieJSON.getBoolean("adult");
            backdropPath = currentMovieJSON.getString("backdrop_path");
            budget = currentMovieJSON.getInt("budget");
            homepage = currentMovieJSON.getString("homepage");
            id = currentMovieJSON.getInt("id");
            imdbId = currentMovieJSON.getString("imdb_id");
            originalLanguage = currentMovieJSON.getString("original_language");
            originalTitle = currentMovieJSON.getString("original_title");
            overview = currentMovieJSON.getString("overview");
            popularity = currentMovieJSON.getDouble("popularity");
            posterPath = currentMovieJSON.getString("poster_path");
            releaseDate = currentMovieJSON.getString("release_date");
            revenue = currentMovieJSON.getInt("revenue");
            runtime = currentMovieJSON.getInt("runtime");
            status = currentMovieJSON.getString("status");
            tagline = currentMovieJSON.getString("tagline");
            title = currentMovieJSON.getString("title");
            video = currentMovieJSON.getBoolean("video");
            voteAverage = currentMovieJSON.getDouble("vote_average");
            voteCount = currentMovieJSON.getInt("vote_count");

            JSONObject belongsToCollectionArray = currentMovieJSON.getJSONObject("belongs_to_collection");

            belongsToCollection = new CurrentMovieRequest.BelongsToCollection(
                    belongsToCollectionArray.getInt("id"),
                    belongsToCollectionArray.getString("name"),
                    belongsToCollectionArray.getString("poster_path"),
                    belongsToCollectionArray.getString("backdrop_path"));

            JSONArray genresArray = currentMovieJSON.getJSONArray("genres");
            for (int countArray = 0; countArray < genresArray.length(); countArray++) {
                JSONObject currentObject = genresArray.getJSONObject(countArray);
                genres.add(new CurrentMovieRequest.Genre(
                        currentObject.getInt("id"),
                        currentObject.getString("name")));
            }

            JSONArray productionCompaniesArray = currentMovieJSON.getJSONArray("production_companies");
            for (int countArray = 0; countArray < productionCompaniesArray.length(); countArray++) {
                JSONObject currentObject = productionCompaniesArray.getJSONObject(countArray);
                productionCompanies.add(new CurrentMovieRequest.ProductionCompany(
                        currentObject.getInt("id"),
                        currentObject.getString("name"),
                        currentObject.getString("logo_path"),
                        currentObject.getString("origin_country")));
            }

            JSONArray productionCountriesArray = currentMovieJSON.getJSONArray("production_countries");
            for (int countArray = 0; countArray < productionCountriesArray.length(); countArray++) {
                JSONObject currentObject = productionCountriesArray.getJSONObject(countArray);
                productionCountries.add(new CurrentMovieRequest.ProductionCountry(
                        currentObject.getString("iso_3166_1"),
                        currentObject.getString("name")));
            }

            JSONArray spokenLanguagesArray = currentMovieJSON.getJSONArray("spoken_languages");
            for (int countArray = 0; countArray < spokenLanguagesArray.length(); countArray++) {
                JSONObject currentObject = spokenLanguagesArray.getJSONObject(countArray);
                spokenLanguages.add(new CurrentMovieRequest.SpokenLanguage(
                        currentObject.getString("iso_639_1"),
                        currentObject.getString("name")));
            }

            return new CurrentMovieRequest(adult,
                    backdropPath,
                    belongsToCollection,
                    budget,
                    genres,
                    homepage,
                    id,
                    imdbId,
                    originalLanguage,
                    originalTitle,
                    overview,
                    popularity,
                    posterPath,
                    productionCompanies,
                    productionCountries,
                    releaseDate,
                    revenue,
                    runtime,
                    spokenLanguages,
                    status,
                    tagline,
                    title,
                    video,
                    voteAverage,
                    voteCount);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DiscoverMovieRequest discoverMovieParce(JSONObject discoverMovieJSON) {
        int page;
        int total_results;
        int total_pages;
        List<DiscoverMovieRequest.ResultsField> resultsFields = new ArrayList<>();

        try {
            page = discoverMovieJSON.getInt("page");
            total_results = discoverMovieJSON.getInt("total_results");
            total_pages = discoverMovieJSON.getInt("total_pages");
            JSONArray jsonArray = discoverMovieJSON.getJSONArray("results");
            for (int countArray = 0; countArray < jsonArray.length(); countArray++) {
                JSONObject currentObject = jsonArray.getJSONObject(countArray);
                List<Integer> genreIdsList = new ArrayList<>();
                JSONArray genreIds = currentObject.getJSONArray("genre_ids");
                for (int i = 0; i < genreIds.length(); i++) {
                    genreIdsList.add(genreIds.getInt(i));
                }

                resultsFields.add(new DiscoverMovieRequest.ResultsField(currentObject.getInt("vote_count"),
                        currentObject.getInt("id"),
                        currentObject.getBoolean("video"),
                        currentObject.getLong("vote_average"),
                        currentObject.getString("title"),
                        currentObject.getLong("popularity"),
                        currentObject.getString("poster_path"),
                        currentObject.getString("original_language"),
                        currentObject.getString("original_title"),
                        genreIdsList,
                        currentObject.getString("backdrop_path"),
                        currentObject.getBoolean("adult"),
                        currentObject.getString("overview"),
                        currentObject.getString("release_date")));
            }
            return new DiscoverMovieRequest(page, total_results, total_pages, resultsFields);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
