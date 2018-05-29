package com.sasfmlzr.findfilm.model.request;
import java.util.List;

@SuppressWarnings({"unused"})
public class CurrentMovieRequest {
    private boolean adult;
    private String backdropPath;
    private BelongsToCollection belongsToCollection;
    private int budget;
    private List<Genre> genres;
    private String homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private List<ProductionCompany> productionCompanies;
    private List<ProductionCountry> productionCountries;
    private String releaseDate;
    private int revenue;
    private int runtime;
    private List<SpokenLanguage> spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public CurrentMovieRequest(boolean adult,
                               String backdropPath,
                               BelongsToCollection belongsToCollection,
                               int budget,
                               List<Genre> genres,
                               String homepage,
                               int id,
                               String imdbId,
                               String originalLanguage,
                               String originalTitle,
                               String overview,
                               double popularity,
                               String posterPath,
                               List<ProductionCompany> productionCompanies,
                               List<ProductionCountry> productionCountries,
                               String releaseDate,
                               int revenue,
                               int runtime,
                               List<SpokenLanguage> spokenLanguages,
                               String status,
                               String tagline,
                               String title,
                               boolean video,
                               double voteAverage,
                               int voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.belongsToCollection = belongsToCollection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spokenLanguages = spokenLanguages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public static class BelongsToCollection {
        private int id;
        private String name;
        private String posterPath;
        private String backdropPath;

        BelongsToCollection(int id, String name, String posterPath, String backdropPath) {
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

    public static class Genre {
        private int id;
        private String name;

        Genre(int id, String name) {
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

    public static class ProductionCompany {
        private int id;
        private Object logoPath;
        private String name;
        private String originCountry;

        ProductionCompany(int id, Object logoPath, String name, String originCountry) {
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

    public static class ProductionCountry {
        private String iso31661;
        private String name;

        ProductionCountry(String iso31661, String name) {
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

    public static class SpokenLanguage {
        private String iso6391;
        private String name;

        SpokenLanguage(String iso6391, String name) {
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
