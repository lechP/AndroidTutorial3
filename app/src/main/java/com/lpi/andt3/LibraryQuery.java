package com.lpi.andt3;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LibraryQuery {

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    private Activity context;
    private BookJSONAdapter localStorage;

    LibraryQuery(Activity activity, BookJSONAdapter localStorage) {
        this.context = activity;
        this.localStorage = localStorage;
    }

    void queryBooks(String searchString) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(QUERY_URL + urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(context.getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                Log.d("omg android", jsonObject.toString());
                localStorage.updateData(jsonObject.optJSONArray("docs"));
            }
            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(context.getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("omg android", statusCode + " " + throwable.getMessage());
            }
        });
    }


}
