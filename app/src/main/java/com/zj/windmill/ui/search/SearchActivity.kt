package com.zj.windmill.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.zj.windmill.databinding.ActivitySearchBinding
import com.zj.windmill.model.Video
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.m = viewModel
        binding.lifecycleOwner = this

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
        binding.rvVideo.adapter = searchPagingAdapter.withLoadStateFooter(SearchLoadStateAdapter {
            viewModel.performSearch()
        })
        viewModel.searchResultFlow.observe(this) {
            lifecycleScope.launch {
                it.collectLatest { pagingData ->
                    searchPagingAdapter.submitData(pagingData)
                }
            }
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onVideoClick(video: Video) {

    }
}