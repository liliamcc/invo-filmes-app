package com.androidpro.invo_filmes_app.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class HttpRequest {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private SyncHttpClient client = new SyncHttpClient();

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        params.put("api_key", "051b2c8e2fdb316bc96106086783abf1");

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
