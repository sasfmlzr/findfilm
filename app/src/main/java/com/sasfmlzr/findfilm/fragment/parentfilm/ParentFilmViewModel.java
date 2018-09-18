package com.sasfmlzr.findfilm.fragment.parentfilm;

import android.databinding.BaseObservable;
import android.support.annotation.IntRange;
import android.support.design.shape.MaterialShapeDrawable;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.design.shape.ShapePathModel;

public class ParentFilmViewModel extends BaseObservable {
    private static final int COLOR_PRIMARY       = 0xFFeaeaee;


    public MaterialShapeDrawable configureLeftButton(@IntRange(from = 0L, to = 255L) int opacity) {
        ShapePathModel leftShapePathModel = new ShapePathModel();
        leftShapePathModel.setBottomLeftCorner(new RoundedCornerTreatment(40));
        leftShapePathModel.setTopLeftCorner(new RoundedCornerTreatment(40));
        MaterialShapeDrawable leftRoundedMaterialShape = new MaterialShapeDrawable(leftShapePathModel);
        leftRoundedMaterialShape.setTint(COLOR_PRIMARY);
        leftRoundedMaterialShape.setAlpha(opacity);
        return leftRoundedMaterialShape;
    }

    public MaterialShapeDrawable configureRightButton(@IntRange(from = 0L, to = 255L) int opacity) {
        ShapePathModel rightShapePathModel = new ShapePathModel();
        rightShapePathModel.setBottomRightCorner(new RoundedCornerTreatment(40));
        rightShapePathModel.setTopRightCorner(new RoundedCornerTreatment(40));
        MaterialShapeDrawable rightRoundedMaterialShape = new MaterialShapeDrawable(rightShapePathModel);
        rightRoundedMaterialShape.setTint(COLOR_PRIMARY);
        rightRoundedMaterialShape.setAlpha(opacity);
        return rightRoundedMaterialShape;
    }
}