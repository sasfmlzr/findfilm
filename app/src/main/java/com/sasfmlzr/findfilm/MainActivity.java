package com.sasfmlzr.findfilm;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
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

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFilmSelectedListener {
    private Menu menu;
    private List<String> items;

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

    private void loadHistory(String query) {
        // Cursor
        String[] columns = new String[]{"_id", "text"};
        Object[] temp = new Object[]{0, "default"};

        MatrixCursor cursor = new MatrixCursor(columns);

        for (int i = 0; i < items.size(); i++) {

            temp[0] = i;
            temp[1] = items.get(i);

            cursor.addRow(temp);

        }
        // SearchView
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSuggestionsAdapter(new SearchAdapter(this, cursor, items));
    }

    @Override
    public void filmClicked(int idFilm) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, CurrentFilmFragment.newInstance(idFilm))
                .addToBackStack(null)
                .commit();
    }
}
