package com.androidpro.invo_filmes_app.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.androidpro.invo_filmes_app.Activities.MainActivity;
import com.androidpro.invo_filmes_app.Activities.MovieListActivity;
import com.androidpro.invo_filmes_app.Models.MovieSummaryModel;
import com.androidpro.invo_filmes_app.Utils.HttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MovieListTask extends AsyncTask<String, Void, ArrayList<MovieSummaryModel>> {

    private HttpRequest _httpRequest;
    private Listener _listener;
    private ArrayList<MovieSummaryModel> _movies;
    private WeakReference<Context> _appContext;

    private ProgressDialog dialog;

    public MovieListTask(Listener listener, Context context) {
        _httpRequest = new HttpRequest();
        _listener = listener;
        _movies = new ArrayList<>();
        _appContext = new WeakReference<>(context);
        dialog = new ProgressDialog(_appContext.get());
    }

    public interface Listener {

        void onLoaded(ArrayList<MovieSummaryModel> movies) throws MalformedURLException;

        void onError();
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Carregando...");
        this.dialog.show();
    }

    @Override
    protected ArrayList<MovieSummaryModel> doInBackground(String... movieName) {
        try {

            if (movieName != null && movieName[0] != "") {
                searchMovies(movieName[0]);
            } else {
                listUpcomingMovies();
            }

            Bitmap bitmap;
            for (MovieSummaryModel movie : _movies
                    ) {
                if (movie.getPosterPath() != null) {
                    InputStream input = new java.net.URL(movie.getPosterPath()).openStream();
                    // Decode Bitmap
                    bitmap = BitmapFactory.decodeStream(input);

                    movie.setPosterImage(bitmap);
                }
            }
            return _movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieSummaryModel> response) {

        if (response != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                _listener.onLoaded(response);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            _listener.onError();
        }
    }

    private void searchMovies(String movieName) {
        try {
            RequestParams params = new RequestParams();
            params.add("query", movieName);
            params.add("page", "1");

            _httpRequest.get("search/movie", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    fillMoviesList(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    super.onFailure(statusCode, headers, throwable, object);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listUpcomingMovies() {
        try {
            _httpRequest.get("movie/upcoming", new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    fillMoviesList(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                    super.onFailure(statusCode, headers, throwable, object);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillMoviesList(JSONObject response) {
        JSONArray json;
        try {
            json = response.getJSONArray("results");
            _movies = new ArrayList<>();
            MovieSummaryModel movie;

            for (int i = 0; i < json.length(); i++) {
                movie = new MovieSummaryModel();
                movie.setId(json.getJSONObject(i).getLong("id"));
                movie.setTitle(json.getJSONObject(i).getString("title"));

                if (json.getJSONObject(i).getString("poster_path") != "null")
                    movie.setPosterPath("https://image.tmdb.org/t/p/w500" + json.getJSONObject(i).getString("poster_path"));
                else
                    movie.setPosterPath(null);

                Date date = new SimpleDateFormat("yyyy-mm-dd").parse(json.getJSONObject(i).getString("release_date"));
                movie.setReleaseDate(new SimpleDateFormat("MMMM dd, yyyy").format(date));
                _movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
