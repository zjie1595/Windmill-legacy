package com.zj.windmill.ui.home

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.setup
import com.youth.banner.indicator.CircleIndicator
import com.zj.windmill.R
import com.zj.windmill.data.remote.HomePageParser
import com.zj.windmill.databinding.ActivityHomeBinding
import com.zj.windmill.model.Video
import com.zj.windmill.ui.BaseActivity
import com.zj.windmill.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    @Inject
    lateinit var homePageParser: HomePageParser

    override fun ActivityHomeBinding.initBinding() {
        val bindingAdapter = rvVideo.divider {
            setDivider(16, true)
            orientation = DividerOrientation.GRID
        }.setup {
            addType<Video>(R.layout.item_video_vertical)
            addType<String>(R.layout.item_video_group_title)
            onClick(R.id.item_video) {
                onVideoClick(getModel())
            }
        }
        (rvVideo.layoutManager as? GridLayoutManager)?.spanSizeLookup =
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
        page.setEnableLoadMore(false)
        page.onRefresh {
            lifecycleScope.launch {
                val homePage = withContext(Dispatchers.IO) {
                    withTimeoutOrNull(10_000L) {
                        homePageParser.parseHomePage()
                    }
                }
                if (homePage == null) {
                    binding.page.showError()
                } else {
                    binding.page.showContent()
                    bindingAdapter.models = homePage.videoGroups
                    val bannerAdapter = HomeBannerAdapter(homePage.bannerVideos) { video ->
                        onVideoClick(video)
                    }
                    binding.banner.addBannerLifecycleObserver(this@HomeActivity)
                        .setAdapter(bannerAdapter).indicator = CircleIndicator(this@HomeActivity)
                }
            }
        }
        page.showLoading()

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun onVideoClick(video: Video) {
//        val videoUrlParser = VideoUrlParser(this)
//        lifecycleScope.launch {
//            val detailPage = withContext(Dispatchers.IO) {
//                detailPageParser.parseDetailPage(video.detailPageUrl)
//            } ?: return@launch
//            Logger.i("detailPage $detailPage")
//            val playlist = detailPage.playlists[1]
//            Logger.i("playlist ${playlist}")
//            val playPageUrl = playlist.episodes.firstOrNull()?.playPageUrl ?: return@launch
//            Logger.i("playPageUrl ${playPageUrl}")
//            val videoUrl = videoUrlParser.parseVideoUrl(playPageUrl)
//            Logger.i("videoUrl $videoUrl")
//        }
    }
}