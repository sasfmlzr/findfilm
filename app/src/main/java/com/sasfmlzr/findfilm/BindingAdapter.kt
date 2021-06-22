package com.sasfmlzr.findfilm

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette

@BindingAdapter("android:text")
fun setText(textView: TextView, number: Double) {
    textView.text = number.toString()
}

@BindingAdapter("android:background")
fun setShadow(linearLayout: LinearLayout, bitmap: Bitmap?) {
    if (bitmap == null) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val palette = Palette.from(bitmap).generate()
        palette.lightMutedSwatch
        val color: Int
        val swatch = palette.lightMutedSwatch
        color = if (swatch != null) {
            swatch.rgb
        } else {
            assert(palette.dominantSwatch != null)
            palette.dominantSwatch!!.rgb
        }
        val radiusValue = 2
        val colors = intArrayOf(
            color,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE
        )
        val shadow = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        shadow.cornerRadius = radiusValue.toFloat()
        shadow.alpha = 150
        linearLayout.background = shadow
    }
}

@BindingAdapter("toastMessage")
fun runMe(view: View, message: String?) {
    if (message != null) Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
}