package com.zj.windmill.ui.play

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zj.windmill.data.remote.DetailPageParser
import com.zj.windmill.databinding.ActivityPlayBinding
import com.zj.windmill.model.Episode
import com.zj.windmill.model.Video
import com.zj.windmill.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlayActivity : BaseActivity<ActivityPlayBinding>() {

//    private val sampleVideoUrl =
//        "https://vip.ffzy-play3.com/20230412/10868_2dee1b0a/index.m3u8"

    @Inject
    lateinit var detailPageParser: DetailPageParser

    override fun ActivityPlayBinding.initBinding() {
//        videoPlayer.setUp(sampleVideoUrl, true, "")
//        videoPlayer.seekOnStart = 10_000L
//        videoPlayer.startPlayLogic()
        parsePlaylists()
    }

    @Suppress("DEPRECATION")
    private fun ActivityPlayBinding.parsePlaylists() {
        state.showLoading()
        val video = intent.getParcelableExtra<Video>("video")
        if (video == null) {
            state.showError()
            return
        }
        lifecycleScope.launch {
            val detailPage = withContext(Dispatchers.IO) {
                detailPageParser.parseDetailPage(video.detailPageUrl)
            }
            if (detailPage == null) {
                state.showError("解析失败")
                return@launch
            }
            state.showContent()
            val episodeFragments = detailPage.playlists.filter {
                it.episodes.isNotEmpty()
            }.map {
                EpisodeFragment.newInstance(it.episodes)
            }
            viewPager.adapter = object : FragmentStateAdapter(this@PlayActivity) {
                override fun getItemCount(): Int {
                    return episodeFragments.size
                }

                override fun createFragment(position: Int): Fragment {
                    return episodeFragments[position]
                }
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val playlist = detailPage.playlists[position]
                tab.text = playlist.title
            }.attach()
        }
    }

    fun onEpisodeClick(episode: Episode) {

    }
}