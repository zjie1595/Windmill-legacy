package com.zj.windmill.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.orhanobut.logger.Logger
import com.youth.banner.indicator.CircleIndicator
import com.zj.windmill.R
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.databinding.ActivityHomeBinding
import com.zj.windmill.model.Error
import com.zj.windmill.model.Loading
import com.zj.windmill.model.None
import com.zj.windmill.model.Success
import com.zj.windmill.model.Video
import com.zj.windmill.ui.play.PlayActivity
import com.zj.windmill.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var detailPageParser: DetailPageParser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bindingAdapter = binding.rvVideo.divider {
            setDivider(16, true)
            orientation = DividerOrientation.GRID
        }.setup {
            addType<Video>(R.layout.item_video_vertical)
            addType<String>(R.layout.item_video_group_title)
            onClick(R.id.item_video) {
                onVideoClick(getModel())
            }
        }
        (binding.rvVideo.layoutManager as? GridLayoutManager)?.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        val model = bindingAdapter.getModel<Any>(position)
                        if (model is Video) {
                            1
                        } else {
                            3
                        }
                    } catch (e: Exception) {
                        1
                    }
                }
            }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collectLatest {
                        when (it.pageState) {
                            is Error -> {
                                binding.state.showError()
                            }

                            Loading -> {
                                binding.state.showLoading()
                            }

                            Success -> {
                                binding.state.showContent()
                            }

                            None -> {

                            }
                        }
                        bindingAdapter.models = it.homePage.videoGroups
                        val bannerVideos = it.homePage.bannerVideos
                        val bannerAdapter = HomeBannerAdapter(bannerVideos) { video ->
                            onVideoClick(video)
                        }
                        binding.banner.addBannerLifecycleObserver(this@HomeActivity)
                            .setAdapter(bannerAdapter)
                            .setIndicator(CircleIndicator(this@HomeActivity))
                    }
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun onVideoClick(video: Video) {
        val videoUrlParser = VideoUrlParser(this)
        lifecycleScope.launch {
            val detailPage = withContext(Dispatchers.IO) {
                detailPageParser.parseDetailPage(video.detailPageUrl)
            } ?: return@launch
            val playlist = detailPage.playlists[detailPage.defaultPlayIndex]
            val playPageUrl = playlist.episodes.firstOrNull()?.playPageUrl ?: return@launch
            val videoUrl = videoUrlParser.parseVideoUrl(playPageUrl)
            Logger.i("videoUrl $videoUrl")
        }
//        startActivity(Intent(this, PlayActivity::class.java))
    }
}