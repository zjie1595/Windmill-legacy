package com.zj.windmill

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import coil.load
import com.blankj.utilcode.util.KeyboardUtils
import com.drake.statelayout.StateLayout
import com.zj.windmill.model.Error
import com.zj.windmill.model.Loading
import com.zj.windmill.model.None
import com.zj.windmill.model.PageState
import com.zj.windmill.model.Success

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:image_url")
    fun loadImageUrl(imageView: ImageView, url: String) {
        imageView.load(url)
    }

    @JvmStatic
    @BindingAdapter("page_state")
    fun bindPageState(stateLayout: StateLayout, pageState: PageState) {
        when (pageState) {
            is Error -> {
                stateLayout.showError()
            }

            Loading -> {
                stateLayout.showLoading()
            }

            None -> {

            }

            Success -> {
                stateLayout.showContent()
            }
        }
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
    @BindingAdapter("soft_key_board_visible")
    fun isSoftKeyboardVisible(editText: EditText, visible: Boolean) {
        if (visible) {
            KeyboardUtils.showSoftInput(editText)
        } else {
            KeyboardUtils.hideSoftInput(editText)
        }
    }
}