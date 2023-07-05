package com.zj.windmill.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.zj.windmill.R
import com.zj.windmill.data.remote.SearchPageParser
import com.zj.windmill.databinding.ActivitySearchBinding
import com.zj.windmill.model.Video
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    @Inject
    lateinit var searchPageParser: SearchPageParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.m = this
        binding.lifecycleOwner = this

        binding.rvVideo.divider {
            setDivider(16, true)
            orientation = DividerOrientation.GRID
        }.setup {
            addType<Video>(R.layout.item_video_vertical)
            onClick(R.id.item_video) {
                onVideoClick(getModel())
            }
        }

        binding.page.onRefresh {
            lifecycleScope.launch {
                val keyword = binding.query.text.toString()
                val searchResult = withContext(Dispatchers.IO) {
                    searchPageParser.parseSearchPage(keyword, index)
                }
                addData(searchResult?.videos) {
                    searchResult?.hasMore == true
                }
            }
        }

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.query.requestFocus()
    }

    fun performSearch() {
        KeyboardUtils.hideSoftInput(binding.query)
        binding.page.showLoading()
    }

    private fun onVideoClick(video: Video) {

    }
}