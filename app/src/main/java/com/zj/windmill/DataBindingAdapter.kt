package com.zj.windmill

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:image_url")
    fun loadImageUrl(imageView: ImageView, url: String) {
        imageView.load(url)
    }
}