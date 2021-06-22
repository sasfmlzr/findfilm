package com.sasfmlzr.findfilm.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DiscoverMovieRequest {
    @SerializedName("page")
    @Expose
    var page = 0
        private set

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
        private set

    @SerializedName("total_results")
    @Expose
    var totalResults = 0
        private set

    @SerializedName("total_pages")
    @Expose
    var totalPages = 0
        private set

    constructor() {}
    constructor(page: Int, results: List<Result>?, totalResults: Int, totalPages: Int) : super() {
        this.page = page
        this.results = results
        this.totalResults = totalResults
        this.totalPages = totalPages
    }

    class Result {
        @SerializedName("poster_path")
        @Expose
        var posterPath: Any? = null
            private set

        @SerializedName("adult")
        @Expose
        var isAdult = false
            private set

        @SerializedName("overview")
        @Expose
        var overview: String? = null
            private set

        @SerializedName("release_date")
        @Expose
        var releaseDate: String? = null
            private set

        @SerializedName("genre_ids")
        @Expose
        var genreIds: List<Int>? = null
            private set

        @SerializedName("id")
        @Expose
        var id = 0
            private set

        @SerializedName("original_title")
        @Expose
        var originalTitle: String? = null
            private set

        @SerializedName("original_language")
        @Expose
        var originalLanguage: String? = null
            private set

        @SerializedName("title")
        @Expose
        var title: String? = null
            private set

        @SerializedName("backdrop_path")
        @Expose
        var backdropPath: Any? = null
            private set

        @SerializedName("popularity")
        @Expose
        var popularity = 0.0
            private set

        @SerializedName("vote_count")
        @Expose
        var voteCount = 0
            private set

        @SerializedName("video")
        @Expose
        var isVideo = false
            private set

        @SerializedName("vote_average")
        @Expose
        var voteAverage = 0.0
            private set

        constructor() {}
        constructor(
            posterPath: Any?,
            adult: Boolean,
            overview: String?,
            releaseDate: String?,
            genreIds: List<Int>?,
            id: Int,
            originalTitle: String?,
            originalLanguage: String?,
            title: String?,
            backdropPath: Any?,
            popularity: Double,
            voteCount: Int,
            video: Boolean,
            voteAverage: Double
        ) : super() {
            this.posterPath = posterPath
            isAdult = adult
            this.overview = overview
            this.releaseDate = releaseDate
            this.genreIds = genreIds
            this.id = id
            this.originalTitle = originalTitle
            this.originalLanguage = originalLanguage
            this.title = title
            this.backdropPath = backdropPath
            this.popularity = popularity
            this.voteCount = voteCount
            isVideo = video
            this.voteAverage = voteAverage
        }
    }
}