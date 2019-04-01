package com.androidpro.invo_filmes_app.Models;

import java.util.ArrayList;

public class MovieDetailModel extends MovieSummaryModel {

    private String overview;
    private ArrayList<GenreModel> genres;

    public MovieDetailModel(){
        genres = new ArrayList<>();
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<GenreModel> getGenres() {
        return genres;
    }

    public void addGenre(GenreModel genre){
        genres.add(genre);
    }

}
