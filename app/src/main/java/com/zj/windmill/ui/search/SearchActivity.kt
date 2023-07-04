package com.zj.windmill.ui.search

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.zj.windmill.databinding.ActivitySearchBinding
import com.zj.windmill.model.Error
import com.zj.windmill.model.Loading
import com.zj.windmill.model.None
import com.zj.windmill.model.Success
import com.zj.windmill.model.Video
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when (it.pageState) {
                        is Error -> {
                            binding.page.showError()
                        }

                        Loading -> {
                            binding.page.showLoading()
                        }

                        Success -> {
                            binding.page.showContent()
                        }

                        None -> {

                        }
                    }
                }
            }
        }
        val searchPagingAdapter = SearchPagingAdapter {
            onVideoClick(it)
        }
        binding.rvVideo.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val size = ConvertUtils.dp2px(8F)
                outRect.left = size
                outRect.right = size
                outRect.top = size
                outRect.bottom = size
            }
        })
        binding.rvVideo.adapter = searchPagingAdapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.flatMapLatest { it.searchResultFlow }
                    .collectLatest {
                        searchPagingAdapter.submitData(it)
                    }
            }
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.search.setOnClickListener {
            performSearch()
        }
        binding.query.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.query.requestFocus()
    }

    private fun performSearch() {
        closeSoftKeyboard()
        val keyword = binding.query.text.toString()
        viewModel.performSearch(keyword)
    }

    private fun onVideoClick(video: Video) {

    }

    private fun closeSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.query.windowToken, 0)
    }
}