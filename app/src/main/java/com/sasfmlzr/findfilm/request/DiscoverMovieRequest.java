package com.sasfmlzr.findfilm.request;
import android.graphics.Bitmap;

import java.util.List;

@SuppressWarnings({"unused"})
public class DiscoverMovieRequest {
    private int page;
    private int total_results;
    private int total_pages;
    private List<ResultsField> resultsFields;


    public DiscoverMovieRequest(int page,
                                int total_results,
                                int total_pages,
                                List<ResultsField> resultsFields) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.resultsFields = resultsFields;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<ResultsField> getResultsFields() {
        return resultsFields;
    }

    public static class ResultsField {
        private int vote_count;
        private int id;
        private Boolean video;
        private float vote_average;
        private String title;
        private float popularity;
        private String poster_path;
        private String original_language;
        private String original_title;
        private List<Integer> genre_ids;
        private String backdrop_path;
        private Boolean adult;
        private String overview;
        private String release_date;
        private Bitmap backdropBitmap;

        ResultsField(int vote_count,
                     int id, Boolean video,
                     float vote_average,
                     String title,
                     float popularity,
                     String poster_path,
                     String original_language,
                     String original_title,
                     List<Integer> genre_ids,
                     String backdrop_path,
                     Boolean adult,
                     String overview,
                     String release_date) {
            this.vote_count = vote_count;
            this.id = id;
            this.video = video;
            this.vote_average = vote_average;
            this.title = title;
            this.popularity = popularity;
            this.poster_path = poster_path;
            this.original_language = original_language;
            this.original_title = original_title;
            this.genre_ids = genre_ids;
            this.backdrop_path = backdrop_path;
            this.adult = adult;
            this.overview = overview;
            this.release_date = release_date;
        }

        public int getVote_count() {
            return vote_count;
        }

        public int getId() {
            return id;
        }

        public Boolean getVideo() {
            return video;
        }

        public float getVote_average() {
            return vote_average;
        }

        public String getTitle() {
            return title;
        }

        public float getPopularity() {
            return popularity;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public Boolean getAdult() {
            return adult;
        }

        public String getOverview() {
            return overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public Bitmap getBackdropBitmap() {
            return backdropBitmap;
        }

        public void setBackdropBitmap(Bitmap backdropBitmap) {
            this.backdropBitmap = backdropBitmap;
        }
    }
}