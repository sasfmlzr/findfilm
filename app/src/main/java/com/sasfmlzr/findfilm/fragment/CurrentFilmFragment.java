package com.sasfmlzr.findfilm.fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentFilmFragment extends Fragment {
    private int idFilm;
    private ImageView posterFilm;
    private TextView nameFilm;
    private TextView description;

    public static CurrentFilmFragment newInstance(int idFilm) {
        Bundle args = new Bundle();
        CurrentFilmFragment fragment = new CurrentFilmFragment();
        args.putInt("idFilm", idFilm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idFilm = getArguments().getInt("idFilm");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_film_fragment, container, false);
        posterFilm = view.findViewById(R.id.current_film_image_view);
        nameFilm = view.findViewById(R.id.name_current_film);
        description = view.findViewById(R.id.description_current_film);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFilm();
    }

    public interface FilmLoaded {
        void isLoaded(CurrentMovieRequest currentMovieRequest);
    }

    public void loadFilm() {
        FilmLoaded callback = (currentMovieRequest) -> {
            posterFilm.setImageBitmap(currentMovieRequest.getBackdropBitmap());
            nameFilm.setText(currentMovieRequest.getTitle());
            description.setText(currentMovieRequest.getOverview());
        };
        new LoadDataFilmTask(idFilm, callback).execute();
    }

    static class LoadDataFilmTask extends AsyncTask<Void, Void, CurrentMovieRequest> {
        private int idFilm;
        private FilmLoaded callback;

        LoadDataFilmTask(int idFilm, FilmLoaded callback) {
            this.idFilm = idFilm;
            this.callback = callback;
        }

        @Override
        protected CurrentMovieRequest doInBackground(Void... voids) {
            try {
                Request request = new Request();
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                String json = request.viewMovie(idFilm);
                JSONObject jsonObject = new JSONObject(json);
                return jsonParserRequest.currentMovieParce(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(CurrentMovieRequest currentMovieRequest) {
            super.onPostExecute(currentMovieRequest);
            callback.isLoaded(currentMovieRequest);
        }
    }
}
