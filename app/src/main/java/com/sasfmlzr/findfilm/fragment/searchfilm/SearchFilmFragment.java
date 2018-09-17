package com.sasfmlzr.findfilm.fragment.searchfilm;

import android.content.Context;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter;
import com.sasfmlzr.findfilm.adapter.SearchAdapter;
import com.sasfmlzr.findfilm.adapter.VerticalItemDecoration;
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;

public class SearchFilmFragment extends Fragment {
    private static final String QUERY_SEARCH_ARGS = "querySearch";
    private static final String RECYCLER_VIEW_TAG = "RecyclerView";
    private String querySearch;

    @BindView(R.id.discoverFilmList)
    public RecyclerView listFilmView;

    public DiscoverFilmFragment.OnFilmSelectedListener filmSelectedListener;
    public boolean isFirstList = true;
    public View view;
    public SearchView searchView;
    public Bundle savedState = null;
    public Unbinder unbinder;
    private Timer timer;
    private Menu menu;

    public static SearchFilmFragment newInstance(String querySearch) {
        Bundle args = new Bundle();
        args.putString(QUERY_SEARCH_ARGS, querySearch);
        SearchFilmFragment fragment = new SearchFilmFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            querySearch = getArguments().getString(QUERY_SEARCH_ARGS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_fragment, container, false);

        savedState = null;
        isFirstList = true;
        unbinder = ButterKnife.bind(this, view);
        loadRecyclerFilmView();

        SearchCallback callback = this::setAdapterDiscoverFilm;
        runSearchRequestFilm(querySearch, callback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public interface SearchCallback {
        void isFind(List<DiscoverMovieRequest.Result> filmList);
    }

    public void loadRecyclerFilmView() {
        listFilmView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listFilmView.addItemDecoration(new VerticalItemDecoration(50));
    }

    public void runSearchRequestFilm(String query, SearchCallback callback) {
        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getSearchMovie(API_KEY, LANGUAGE, query, 1)
                .enqueue(new Callback<DiscoverMovieRequest>() {
                    @Override
                    public void onResponse(Call<DiscoverMovieRequest> call, Response<DiscoverMovieRequest> response) {
                        DiscoverMovieRequest discoverMovieRequest = response.body();
                        callback.isFind(discoverMovieRequest.getResults());
                    }

                    @Override
                    public void onFailure(Call<DiscoverMovieRequest> call, Throwable t) {
                    }
                });
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
                        runSearchRequestFilm(query, callback);
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

    private void setAdapterDiscoverFilm(List<DiscoverMovieRequest.Result> filmList) {
        DiscoverFilmFragment.RecyclerElementEnded callback = () ->
                Log.d(RECYCLER_VIEW_TAG, "setAdapterDiscoverFilm()");
        RecyclerView.Adapter adapter =
                new DiscoverRecyclerAdapter(filmList, filmSelectedListener, callback);
        listFilmView.setAdapter(adapter);
    }
}
