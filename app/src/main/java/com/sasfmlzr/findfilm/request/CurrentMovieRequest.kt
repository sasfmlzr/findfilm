package com.sasfmlzr.findfilm.request

import android.graphics.Bitmap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentMovieRequest {
    @SerializedName("adult")
    @Expose
    var isAdult = false
        private set

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
        private set

    @SerializedName("belongs_to_collection")
    @Expose
    var belongsToCollection: Any? = null
        private set

    @SerializedName("budget")
    @Expose
    var budget = 0
        private set

    @SerializedName("genres")
    @Expose
    var genres: List<Genre>? = null
        private set

    @SerializedName("homepage")
    @Expose
    var homepage: String? = null
        private set

    @SerializedName("id")
    @Expose
    var id = 0
        private set

    @SerializedName("imdb_id")
    @Expose
    var imdbId: String? = null
        private set

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null
        private set

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null
        private set

    @SerializedName("overview")
    @Expose
    var overview: String? = null
        private set

    @SerializedName("popularity")
    @Expose
    var popularity = 0.0
        private set

    @SerializedName("poster_path")
    @Expose
    var posterPath: Any? = null
        private set

    @SerializedName("production_companies")
    @Expose
    var productionCompanies: List<ProductionCompany>? = null
        private set

    @SerializedName("production_countries")
    @Expose
    var productionCountries: List<ProductionCountry>? = null
        private set

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
        private set

    @SerializedName("revenue")
    @Expose
    var revenue = 0.0
        private set

    @SerializedName("runtime")
    @Expose
    var runtime = 0
        private set

    @SerializedName("spoken_languages")
    @Expose
    var spokenLanguages: List<SpokenLanguage>? = null
        private set

    @SerializedName("status")
    @Expose
    var status: String? = null
        private set

    @SerializedName("tagline")
    @Expose
    var tagline: String? = null
        private set

    @SerializedName("title")
    @Expose
    var title: String? = null
        private set

    @SerializedName("video")
    @Expose
    var isVideo = false
        private set

    @SerializedName("vote_average")
    @Expose
    var voteAverage = 0.0
        private set

    @SerializedName("vote_count")
    @Expose
    var voteCount = 0
        private set
    var backdropBitmap: Bitmap? = null

    constructor() {}
    constructor(
        adult: Boolean,
        backdropPath: String?,
        belongsToCollection: Any?,
        budget: Int,
        genres: List<Genre>?,
        homepage: String?,
        id: Int,
        imdbId: String?,
        originalLanguage: String?,
        originalTitle: String?,
        overview: String?,
        popularity: Double,
        posterPath: Any?,
        productionCompanies: List<ProductionCompany>?,
        productionCountries: List<ProductionCountry>?,
        releaseDate: String?,
        revenue: Int,
        runtime: Int,
        spokenLanguages: List<SpokenLanguage>?,
        status: String?,
        tagline: String?,
        title: String?,
        video: Boolean,
        voteAverage: Double,
        voteCount: Int
    ) : super() {
        isAdult = adult
        this.backdropPath = backdropPath
        this.belongsToCollection = belongsToCollection
        this.budget = budget
        this.genres = genres
        this.homepage = homepage
        this.id = id
        this.imdbId = imdbId
        this.originalLanguage = originalLanguage
        this.originalTitle = originalTitle
        this.overview = overview
        this.popularity = popularity
        this.posterPath = posterPath
        this.productionCompanies = productionCompanies
        this.productionCountries = productionCountries
        this.releaseDate = releaseDate
        this.revenue = revenue.toDouble()
        this.runtime = runtime
        this.spokenLanguages = spokenLanguages
        this.status = status
        this.tagline = tagline
        this.title = title
        isVideo = video
        this.voteAverage = voteAverage
        this.voteCount = voteCount
    }

    class Genre {
        @SerializedName("id")
        @Expose
        var id = 0
            private set

        @SerializedName("name")
        @Expose
        var name: String? = null
            private set

        constructor() {}
        constructor(id: Int, name: String?) : super() {
            this.id = id
            this.name = name
        }
    }

    class ProductionCompany {
        @SerializedName("id")
        @Expose
        var id = 0
            private set

        @SerializedName("logo_path")
        @Expose
        var logoPath: String? = null
            private set

        @SerializedName("name")
        @Expose
        var name: String? = null
            private set

        @SerializedName("origin_country")
        @Expose
        var originCountry: String? = null
            private set

        constructor() {}
        constructor(id: Int, logoPath: String?, name: String?, originCountry: String?) : super() {
            this.id = id
            this.logoPath = logoPath
            this.name = name
            this.originCountry = originCountry
        }
    }

    class ProductionCountry {
        @SerializedName("iso_3166_1")
        @Expose
        var iso31661: String? = null
            private set

        @SerializedName("name")
        @Expose
        var name: String? = null
            private set

        constructor() {}
        constructor(iso31661: String?, name: String?) : super() {
            this.iso31661 = iso31661
            this.name = name
        }
    }

    class SpokenLanguage {
        @SerializedName("iso_639_1")
        @Expose
        var iso6391: String? = null
            private set

        @SerializedName("name")
        @Expose
        var name: String? = null
            private set

        constructor() {}
        constructor(iso6391: String?, name: String?) : super() {
            this.iso6391 = iso6391
            this.name = name
        }
    }
}