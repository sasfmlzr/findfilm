package com.sasfmlzr.findfilm;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.sasfmlzr.findfilm.adapter.SearchAdapter;
import com.sasfmlzr.findfilm.fragment.CurrentFilmFragment;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.fragment.DiscoverFilmFragment.OnFilmSelectedListener;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.JsonParserRequest;
import com.sasfmlzr.findfilm.request.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFilmSelectedListener {
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.containerForFragment, new DiscoverFilmFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        this.menu = menu;
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                loadHistory(query);
                return true;
            }
        });
        return true;
    }

    @Override
    public void filmClicked(int idFilm) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, CurrentFilmFragment.newInstance(idFilm))
                .addToBackStack(null)
                .commit();
    }

    public interface SearchCallback {
        void isFind(List<DiscoverMovieRequest.ResultsField> filmList);
    }

    private void loadHistory(String query) {
        // Cursor
        SearchCallback callback = filmList -> {
            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "default"};

            MatrixCursor cursor = new MatrixCursor(columns);

            for (int i = 0; i < filmList.size(); i++) {
                temp[0] = i;
                temp[1] = filmList.get(i);
                cursor.addRow(temp);
            }

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSuggestionsAdapter(new SearchAdapter(getApplicationContext(), cursor, filmList));
        };

        new SearchFilmTask(query, callback).execute();
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
}
