package com.androidpro.invo_filmes_app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidpro.invo_filmes_app.Models.GenreModel;
import com.androidpro.invo_filmes_app.Models.MovieDetailModel;
import com.androidpro.invo_filmes_app.R;
import com.androidpro.invo_filmes_app.Tasks.MovieDetailsTask;
import com.androidpro.invo_filmes_app.Utils.Utils;

import java.net.MalformedURLException;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsTask.Listener {

    private ImageView _image;
    private TextView _title;
    private TextView _genres;
    private TextView _releaseDate;
    private TextView _overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLocaleLanguaje(getBaseContext());

        setContentView(R.layout.activity_movies_details);
        Intent intent = getIntent();

        _image = findViewById(R.id.detailsPictureImg);
        _title = findViewById(R.id.detailsPictureTitle);
        _genres = findViewById(R.id.detailsPictureGenres);
        _releaseDate = findViewById(R.id.detailsPictureReleaseDate);
        _overview = findViewById(R.id.detailsPictureOverview);

        new MovieDetailsTask(this, this).execute(intent.getLongExtra("id", 0));
    }

    @Override
    public void onLoaded(MovieDetailModel movie) throws MalformedURLException {

        if(movie.getPosterImage() != null)
            _image.setImageBitmap(movie.getPosterImage());

        for (GenreModel genre: movie.getGenres()
             ) {
            _genres.setText(_genres.getText() + " " + genre.getName());
        }

        _title.setText(movie.getTitle());
        _releaseDate.setText(movie.getReleaseDate());
        _overview.setText(movie.getOverview());
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Erro no carregamento do filme !", Toast.LENGTH_SHORT).show();
    }
}
