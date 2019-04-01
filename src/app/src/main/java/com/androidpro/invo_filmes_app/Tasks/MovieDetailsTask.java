package com.androidpro.invo_filmes_app.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.androidpro.invo_filmes_app.Models.GenreModel;
import com.androidpro.invo_filmes_app.Models.MovieDetailModel;
import com.androidpro.invo_filmes_app.Utils.HttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsTask extends AsyncTask<Long, Void, MovieDetailModel> {

    private HttpRequest _httpRequest;
    private Listener _listener;
    private MovieDetailModel _movie;
    private WeakReference<Context> _appContext;

    private ProgressDialog dialog;

    public MovieDetailsTask(Listener listener, Context context) {
        _httpRequest = new HttpRequest();
        _listener = listener;
        _appContext = new WeakReference<>(context);
        dialog = new ProgressDialog(_appContext.get());
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Carregando...");
        this.dialog.show();
    }

    @Override
    protected MovieDetailModel doInBackground(Long... id) {
        try {
            getMovieById(id[0]);

            InputStream input = new URL(_movie.getPosterPath()).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            _movie.setPosterImage(bitmap);
            return _movie;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieDetailModel response) {

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

    public interface Listener {

        void onLoaded(MovieDetailModel movie) throws MalformedURLException;

        void onError();
    }

    private void getMovieById(Long id) {
        try {
            _httpRequest.get("movie/" + id, new RequestParams(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        _movie = new MovieDetailModel();
                        _movie.setId(response.getLong("id"));
                        _movie.setTitle(response.getString("title"));
                        _movie.setOverview(response.getString("overview"));
                        _movie.setPosterPath("https://image.tmdb.org/t/p/w500" + response.getString("poster_path"));

                        JSONArray genres = response.getJSONArray("genres");

                        for (int i = 0; i < genres.length(); i++) {
                            _movie.addGenre(new GenreModel(genres.getJSONObject(i).getInt("id"), genres.getJSONObject(i).getString("name")));
                        }

                        Date date = new SimpleDateFormat("yyyy-mm-dd").parse(response.getString("release_date"));
                        _movie.setReleaseDate(new SimpleDateFormat("MMMM dd, yyyy").format(date));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
}
