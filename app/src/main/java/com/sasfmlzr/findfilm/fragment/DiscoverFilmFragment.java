package com.sasfmlzr.findfilm.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.adapter.SearchAdapter;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.Request;
import com.sasfmlzr.findfilm.utils.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_154PX;

public class DiscoverFilmFragment extends AbstractFilmFragment {

    private Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_fragment, container, false);
        setHasOptionsMenu(true);
        countLoadedPages = 1;
        isFirstList = true;
        loadRecyclerFilmView();

        new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println();
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        this.menu = menu;

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(
                getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchCallback callback = filmList -> {
                   /* isFirstList = true;
                    setAdapterDiscoverFilm(filmList);
                    for (DiscoverMovieRequest.ResultsField film : filmList) {
                        new DownloadImageTask(film, downloadCallback)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }*/
                   filmSelectedListener.filmSearched();
                };
                new SearchFilmTask(query, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    public interface SearchCallback {
        void isFind(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    private void loadHistory(String query) {
        // Cursor
        if (query.length() == 0) {
            String[] columns = new String[]{"_id", "text"};
            MatrixCursor cursor = new MatrixCursor(columns);
            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSuggestionsAdapter(new SearchAdapter(getContext(),
                    cursor,
                    null,
                    filmSelectedListener));
            return;
        }
        if (query.length() > 2) {
            SearchCallback callback = filmList -> {
                String[] columns = new String[]{"_id", "text"};
                Object[] temp = new Object[]{0, "default"};

                MatrixCursor cursor = new MatrixCursor(columns);

                for (int i = 0; i < filmList.size(); i++) {
                    temp[0] = i;
                    temp[1] = filmList.get(i);
                    cursor.addRow(temp);
                }
                final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
                search.setSuggestionsAdapter(new SearchAdapter(getContext(),
                        cursor,
                        filmList,
                        filmSelectedListener));
            };
            new SearchFilmTask(query, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private static class SearchFilmTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private String query;
        private SearchCallback callback;

        SearchFilmTask(String query, SearchCallback callback) {
            this.query = query;
            this.callback = callback;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            Request request = new Request();
            try {
                JSONObject jsonObject = new JSONObject(request.searchMovie(query));
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

    public interface OnFilmSelectedListener {
        void filmClicked(int idFilm);
        void filmSearched();
    }

    public interface FilmListComplete {
        void isCompleted(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    public interface DownloadImage {
        void isDownloaded(DiscoverMovieRequest.ResultsField film);
    }

    public interface RecyclerElementEnded {
        void isEnded();
    }

    private DownloadImage downloadCallback = (film) -> {
        DiscoverRecyclerAdapter adapter = (DiscoverRecyclerAdapter) listFilmView.getAdapter();
        if (adapter != null) {
            adapter.replaceImageViewFilm(film);
        }
    };

    private FilmListComplete setFilmListListener() {
        return (filmList) -> {
            setAdapterDiscoverFilm(filmList);
            countLoadedPages++;
            for (DiscoverMovieRequest.ResultsField film : filmList) {
                new DownloadImageTask(film, downloadCallback)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
    }

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.ResultsField> filmList) {
        if (isFirstList) {
            DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                    new RetrieveFeedTask(countLoadedPages, setFilmListListener())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            RecyclerView.Adapter adapter =
                    new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
            listFilmView.setAdapter(adapter);
            isFirstList = false;
        } else {
            ((DiscoverRecyclerAdapter) listFilmView.getAdapter()).addElements(filmList);
        }
    }

    static class RetrieveFeedTask extends AsyncTask<Void, Void, List<DiscoverMovieRequest.ResultsField>> {
        private int countLoadedPages;
        private FilmListComplete listener;

        RetrieveFeedTask(int countLoadedPages, FilmListComplete listener) {
            this.countLoadedPages = countLoadedPages;
            this.listener = listener;
        }

        @Override
        protected List<DiscoverMovieRequest.ResultsField> doInBackground(Void... voids) {
            Request request = new Request();
            String json = "";
            JSONObject jsonObject;
            try {
                JsonParserRequest jsonParserRequest = new JsonParserRequest();
                json = request.discoverMovie(countLoadedPages);
                jsonObject = new JSONObject(json);
                DiscoverMovieRequest movieRequest = jsonParserRequest.discoverMovieParce(jsonObject);
                return movieRequest.getResultsFields();
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
