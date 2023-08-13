package com.zj.windmill

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:image_url")
    fun loadImageUrl(imageView: ImageView, url: String) {
        imageView.load(url)
    }

    @JvmStatic
    @BindingAdapter("search_action")
    fun bindSearchAction(editText: EditText, performSearch: () -> Unit) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:selected")
    fun bindViewSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }
}