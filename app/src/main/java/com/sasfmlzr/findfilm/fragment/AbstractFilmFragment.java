package com.sasfmlzr.findfilm.fragment;
import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.adapter.SearchAdapter;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.RequestMovie;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_154PX;

public abstract class AbstractFilmFragment extends android.support.v4.app.Fragment {
    public DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    public boolean isFirstList = true;
    public RecyclerView listFilmView;
    public View view;
    public SearchView searchView;
    public Bundle savedState = null;
    private Timer timer;
    private Menu menu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            filmSelectedListener = (DiscoverFilmFragment.OnFilmSelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " OnFilmSelectedListener not attached");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filmSelectedListener = null;
    }

    public interface RecyclerElementEnded {
        void isEnded();
    }

    public interface SearchCallback {
        void isFind(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    public DiscoverFilmFragment.DownloadImage downloadCallback = (film) -> {
        DiscoverRecyclerAdapter adapter = (DiscoverRecyclerAdapter) listFilmView.getAdapter();
        if (adapter != null) {
            adapter.replaceImageViewFilm(film);
        }
    };

    public void loadRecyclerFilmView() {
        listFilmView = view.findViewById(R.id.discoverFilmList);
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public Bundle saveState() {
        Bundle state = new Bundle();
        if (searchView != null) {
            state.putString("currentSearchQuery", searchView.getQuery().toString());
        }
        return state;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    filmSelectedListener.filmSearched(query);
                    InputMethodManager imm = (InputMethodManager)
                            Objects.requireNonNull(getActivity())
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                loadHistory(query);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadHistory(String query) {
        String[] columns = new String[]{"_id", "text"};
        Object[] temp = new Object[]{0, "default"};
        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        MatrixCursor cursor = new MatrixCursor(columns);
        if (query.length() == 0) {
            search.setSuggestionsAdapter(new SearchAdapter(getContext(),
                    cursor,
                    null,
                    filmSelectedListener));
            return;
        }
        if (query.length() > 2) {
            SearchCallback callback = filmList -> {
                for (int i = 0; i < filmList.size(); i++) {
                    temp[0] = i;
                    temp[1] = filmList.get(i);
                    cursor.addRow(temp);
                }
                search.setSuggestionsAdapter(new SearchAdapter(getContext(),
                        cursor,
                        filmList,
                        filmSelectedListener));
            };
            Handler handler = new Handler();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        new SearchFilmTask(query, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        timer = null;
                    });
                }
            };
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(timerTask, 2000);
        }
    }

    static class SearchFilmTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private String query;
        private SearchCallback callback;

        SearchFilmTask(String query, SearchCallback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            RequestMovie requestMovie = new RequestMovie();
            try {
                JSONObject jsonObject = new JSONObject(requestMovie.searchMovie(query));
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                DiscoverMovieRequest result = jsonParserRequest.searchMovieParce(jsonObject);
                return result.getResultsFields();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DiscoverMovieRequest.ResultsField> resultsFields) {
            callback.isFind(resultsFields);
            super.onPostExecute(resultsFields);
        }
    }

    static class DownloadImageTask extends AsyncTask<Void, Void, DiscoverMovieRequest.ResultsField> {
        private DiscoverMovieRequest.ResultsField film;
        private DiscoverFilmFragment.DownloadImage callback;
        private String url;

        DownloadImageTask(DiscoverMovieRequest.ResultsField film, DiscoverFilmFragment.DownloadImage callback) {
            if (film.getBackdrop_path().equals("null")) {
                this.url = URL_IMAGE_154PX + film.getPoster_path();
            } else {
                this.url = URL_IMAGE_154PX + film.getBackdrop_path();
            }
            this.film = film;
            this.callback = callback;
        }

        @Override
        protected DiscoverMovieRequest.ResultsField doInBackground(Void... voids) {
            Bitmap bitmap = Downloader.downloadImage(url);
            film.setBackdropBitmap(bitmap);
            return film;
        }

        @Override
        protected void onPostExecute(DiscoverMovieRequest.ResultsField film) {
            callback.isDownloaded(film);
            super.onPostExecute(film);
        }
    }
}
