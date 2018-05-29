package com.sasfmlzr.findfilm.model.fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.Request;
import com.sasfmlzr.findfilm.model.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.model.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.model.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.model.request.JsonParserRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DiscoverFilmFragment extends android.support.v4.app.Fragment {
    private RecyclerView listFilmView;
    private View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        loadRecyclerFilmView();
        AsyncComplete listener = (List<DiscoverMovieRequest.ResultsField> filmList) -> {
            setAdapterDiscoverFilm(filmList);
        };
        new RetrieveFeedTask(listener).execute();
        return view;
    }

    public interface AsyncComplete{
        void isCompleted(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    private void loadRecyclerFilmView(){
        listFilmView=view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList){
        RecyclerView.Adapter adapter = new DiscoverRecyclerAdapter(filmList);
        listFilmView.setAdapter(adapter);
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private AsyncComplete listener;

        RetrieveFeedTask(AsyncComplete listener) {
            this.listener=listener;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
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
                return request1.getResultsFields();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(json);
            return null;
        }

        @Override
        protected void onPostExecute(List<DiscoverMovieRequest.ResultsField> resultsFields) {
            listener.isCompleted(resultsFields);
            super.onPostExecute(resultsFields);
        }
    }
}
