package com.sasfmlzr.findfilm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.sasfmlzr.findfilm.model.fragment.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.model.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.model.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.model.request.JsonParserRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RetrieveFeedTask().execute();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.containerForFragment, new DiscoverFilmFragment())
                    .commit();
        }
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Request request = new Request();
            String json = "";
            JSONObject jsonObject;
            try {
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                json = request.discoverMovie();
                jsonObject = new JSONObject(json);
                DiscoverMovieRequest request1 = jsonParserRequest.discoverMovieParce(jsonObject);
                json = request.viewMovie(383498);
                jsonObject = new JSONObject(json);
                CurrentMovieRequest currentMovieRequest = jsonParserRequest.currentMovieParce(jsonObject);
                System.out.println();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(json);
            return null;
        }
    }
}
