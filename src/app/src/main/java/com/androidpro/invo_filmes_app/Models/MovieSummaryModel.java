package com.androidpro.invo_filmes_app.Models;

import android.graphics.Bitmap;

public class MovieSummaryModel {
    private long id;
    private String posterPath;
    private String title;
    private String releaseDate;
    private Bitmap posterImage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Bitmap getPosterImage() {
        return posterImage;
    }
    public void setPosterImage(Bitmap posterImage) {
        this.posterImage = posterImage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
