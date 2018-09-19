package com.sasfmlzr.findfilm.fragment.currentfilm;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sasfmlzr.findfilm.R;
import com.sasfmlzr.findfilm.databinding.CurrentFilmFragmentBinding;

import java.util.Objects;

public class CurrentFilmFragment extends Fragment {
    public static final String ARGUMENT_FILM_ID = "idFilm";
    private CurrentFilmViewModel viewModel;
    private CurrentFilmFragmentBinding viewDataBinding;

    public static CurrentFilmFragment newInstance(int idFilm) {
        Bundle args = new Bundle();
        CurrentFilmFragment fragment = new CurrentFilmFragment();
        args.putInt(ARGUMENT_FILM_ID, idFilm);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_film_fragment, container, false);

        viewModel = new CurrentFilmViewModel();
        viewDataBinding = CurrentFilmFragmentBinding.bind(view);
        viewDataBinding.setViewmodel(viewModel);

        setupToolbar();

        return viewDataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current_film_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            viewModel.start(getArguments().getInt(ARGUMENT_FILM_ID));
        }
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = viewDataBinding.getRoot().findViewById(R.id.current_film_toolbar);
        setHasOptionsMenu(true);
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(item -> activity.onBackPressed());
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    @BindingAdapter("android:text")
    public static void setText(TextView textView, double number) {
        textView.setText(String.valueOf(number));
    }

    @BindingAdapter("android:src")
    public static void setPosterFilm(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:background")
    public static void setShadow(LinearLayout linearLayout, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Palette palette = Palette.from(bitmap).generate();
            palette.getLightMutedSwatch();
            int color;
            Palette.Swatch swatch = palette.getLightMutedSwatch();
            if (swatch != null) {
                color = swatch.getRgb();
            } else {
                assert palette.getDominantSwatch() != null;
                color = palette.getDominantSwatch().getRgb();
            }

            int radiusValue = 2;
            int[] colors = {color, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
            GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
            shadow.setCornerRadius(radiusValue);
            shadow.setAlpha(150);
            linearLayout.setBackground(shadow);
        }
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
