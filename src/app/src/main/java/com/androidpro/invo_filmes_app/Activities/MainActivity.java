package com.androidpro.invo_filmes_app.Activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.androidpro.invo_filmes_app.Models.MovieSummaryModel;
import com.androidpro.invo_filmes_app.R;
import com.androidpro.invo_filmes_app.Tasks.MovieListTask;
import com.androidpro.invo_filmes_app.Utils.ExtendedSimpleAdapter;
import com.androidpro.invo_filmes_app.Utils.Utils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListTask.Listener, AdapterView.OnItemClickListener {

    private ListView _moviesListView;
    private ArrayList<MovieSummaryModel> _movies;
    private List<HashMap<String, Object>> _moviesMapList;
    private EditText _search;

    private static final String KEY_IMG = "pictureImg";
    private static final String KEY_TITLE = "pictureTitle";
    private static final String KEY_DATE = "pictureReleaseDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLocaleLanguaje(getBaseContext());

        setContentView(R.layout.activity_main);

        _search = findViewById(R.id.search);

        _search.addTextChangedListener(searchWatcher);

        _moviesListView = findViewById(R.id.moviesListView);
        _moviesListView.setOnItemClickListener(this);
        new MovieListTask(this, this).execute("");
    }

    @Override
    public void onLoaded(ArrayList<MovieSummaryModel> movies) throws MalformedURLException {
        _movies = movies;
        _moviesMapList = new ArrayList<>();

        for (MovieSummaryModel movie : _movies) {
            HashMap<String, Object> map = new HashMap<>();

            map.put(KEY_IMG, movie.getPosterImage());
            map.put(KEY_TITLE, movie.getTitle());
            map.put(KEY_DATE, movie.getReleaseDate());

            _moviesMapList.add(map);
        }
        loadMovieListView();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Erro no carregamento dos filmes !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("id", _movies.get(i).getId());
        startActivity(intent);
    }

    private void loadMovieListView() {

        ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(MainActivity.this, _moviesMapList, R.layout.activity_movies_list,
                new String[]{KEY_IMG, KEY_TITLE, KEY_DATE},
                new int[]{R.id.pictureImg, R.id.pictureTitle, R.id.pictureReleaseDate});

        _moviesListView.setAdapter(adapter);
    }

    private final TextWatcher searchWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable;

        @Override
        public void afterTextChanged(final Editable s) {
            if (s.length() > 0) {
            runnable = new Runnable() {
                @Override
                public void run() {
                        new MovieListTask(MainActivity.this, MainActivity.this).execute(s.toString());
                        handler.removeCallbacks(runnable);
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        }
    };
}
