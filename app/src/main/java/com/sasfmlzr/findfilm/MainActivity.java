package com.sasfmlzr.findfilm;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RetrieveFeedTask().execute();
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Request request = new Request();
            String json="";
            JSONObject jsonObject;
            try {
                json = request.connection("https://api.themoviedb.org/3/movie/550?api_key=95b6fb97b8ac374f68020711a8b9ceec");
                jsonObject = new JSONObject(json);
                System.out.println();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            System.out.println(json);
            return null;
        }
    }
}
