package com.sasfmlzr.findfilm.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {

    private int space = 10;

    public VerticalItemDecoration(int value) {
        this.space = value;
    }

    public VerticalItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = space;
    }
}