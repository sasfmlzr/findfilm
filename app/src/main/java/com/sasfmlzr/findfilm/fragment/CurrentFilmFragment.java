package com.sasfmlzr.findfilm.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.model.RetrofitSingleton;
import com.sasfmlzr.findfilm.request.CurrentMovieRequest;
import com.sasfmlzr.findfilm.request.FindFilmApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;
import static com.sasfmlzr.findfilm.model.SystemSettings.LANGUAGE;
import static com.sasfmlzr.findfilm.model.SystemSettings.URL_IMAGE_500PX;

public class CurrentFilmFragment extends Fragment {
    private int idFilm;
    private Unbinder unbinder;

    @BindView(R.id.button_play_video)
    ImageButton buttonPlayVideo;
    @BindView(R.id.linear_layout_parent)
    LinearLayout linearLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.current_film_image_view)
    ImageView posterFilm;
    @BindView(R.id.description_current_film)
    TextView description;
    @BindView(R.id.vote_average)
    TextView voteAverage;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.progressBarLoaderCurrentFilm)
    ProgressBar progressLoaderImage;
    @BindView(R.id.current_film_toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.button_buy_tickets)
    MaterialButton buttonBuyTickets;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_film_fragment, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(item -> activity.onBackPressed());
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        Drawable drawable = getResources().getDrawable(R.drawable.baseline_favorite_24);
        int color = 0xFFAD563B;
        drawable.setTint(color);
        voteAverage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        voteAverage.setCompoundDrawablePadding(20);
        buttonBuyTickets.setOnClickListener(item -> Toast.makeText
                (getContext(), "Buy tickets pressed", Toast.LENGTH_SHORT).show());
        buttonPlayVideo.setOnClickListener(item ->
                Toast.makeText(getContext(), "Play video is pressed", Toast.LENGTH_SHORT).show());
        setVisibleItems(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFilm();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current_film_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface FilmLoaded {
        void isLoaded(CurrentMovieRequest currentMovieRequest);
    }

    public interface DownloadImage {
        void isDownloaded(CurrentMovieRequest currentMovieRequest);
    }

    public void loadFilm() {
        DownloadImage imageDownloadCallback = (film) -> {
            Bitmap imageFilm = film.getBackdropBitmap();
            posterFilm.setImageBitmap(imageFilm);
            linearLayout.setBackground(createShadow(imageFilm));
            progressLoaderImage.setVisibility(View.INVISIBLE);
        };

        FilmLoaded filmLoadCallback = (currentMovieRequest) -> {
            collapsingToolbarLayout.setTitle(currentMovieRequest.getTitle());
            releaseDate.setText(currentMovieRequest.getReleaseDate());
            voteAverage.setText(String.valueOf(currentMovieRequest.getVoteAverage()));
            description.setText(currentMovieRequest.getOverview());
            setVisibleItems(true);
            String url;
            if (currentMovieRequest.getBackdropPath() == null) {
                url = URL_IMAGE_500PX + currentMovieRequest.getPosterPath();
            } else {
                url = URL_IMAGE_500PX + currentMovieRequest.getBackdropPath();
            }
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    currentMovieRequest.setBackdropBitmap(bitmap);
                    imageDownloadCallback.isDownloaded(currentMovieRequest);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.get().load(url).into(target);
        };

        FindFilmApi findFilmApi = RetrofitSingleton.getFindFilmApi();
        findFilmApi.getCurrentMovie(idFilm, API_KEY, LANGUAGE)
                .enqueue(new Callback<CurrentMovieRequest>() {
                    @Override
                    public void onResponse(Call<CurrentMovieRequest> call, Response<CurrentMovieRequest> response) {
                        filmLoadCallback.isLoaded(response.body());
                    }

                    @Override
                    public void onFailure(Call<CurrentMovieRequest> call, Throwable t) {

                    }
                });
    }

    private void setVisibleItems(boolean visible) {
        int visibleItem;
        if (visible) {
            visibleItem = View.VISIBLE;
        } else {
            visibleItem = View.INVISIBLE;
        }
        posterFilm.setVisibility(visibleItem);
        description.setVisibility(visibleItem);
        voteAverage.setVisibility(visibleItem);
        releaseDate.setVisibility(visibleItem);
        buttonBuyTickets.setVisibility(visibleItem);
    }

    private Drawable createShadow(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Palette palette = Palette.from(bitmap).generate();
            palette.getLightMutedSwatch();
            int color = Objects.requireNonNull(palette.getLightMutedSwatch()).getRgb();
            int radiusValue = 2;
            int[] colors = {color, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            shadow.setCornerRadius(radiusValue);
            shadow.setAlpha(150);
            return shadow;
        } else {
            return null;
        }
    }
}
