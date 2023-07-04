package com.zj.windmill.data.remote

import com.zj.windmill.model.HomePage
import com.zj.windmill.model.Video
import com.zj.windmill.model.VideoGroup
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import javax.inject.Inject

class HomePageParser @Inject constructor(
    okHttpClient: OkHttpClient
) : PageParser(okHttpClient) {

    /**
     * 解析主页
     * @return [HomePage?]
     */
    fun parseHomePage(): HomePage? {
        val document = fetchDocument(host) ?: return null
        return HomePage(
            bannerVideos = parseBannerVideos(document),
            videoGroups = parseVideoGroups(document),
            weeklyRankingVideos = parseWeeklyRankingVideos(document)
        )
    }

    /**
     * 解析广告视频
     * @param [document] 文档
     * @return [List<Video>]
     */
    private fun parseBannerVideos(document: Document): List<Video> {
        val bannerElements = document.select("body > div.foucs.bg > div.hero-wrap > ul > li")
        return parseVideosFromElements(bannerElements)
    }

    /**
     * 解析视频组
     * @param [document] 文档
     * @return [List<VideoGroup>]
     */
    private fun parseVideoGroups(document: Document): List<VideoGroup> {
        val groupElements = document.select("body > div:nth-child(11) > div.firs.l > dtit")
        return groupElements.map { dtit ->
            val groupTitle = dtit.select("h2 > a").text()
            val nextElementSibling = dtit.nextElementSibling() ?: return@map VideoGroup(
                title = groupTitle,
                videos = emptyList()
            )
            val videoElements = nextElementSibling.select("ul > li")
            val videos = parseVideosFromElements(videoElements)
            VideoGroup(title = groupTitle, videos = videos)
        }
    }

    /**
     * 解析每周排名视频
     * @param [document] 文档
     * @return [List<Video>]
     */
    private fun parseWeeklyRankingVideos(document: Document): List<Video> {
        val rankingElements =
            document.select("body > div:nth-child(11) > div.side.r > div:nth-child(3) > div.pics > ul > li")
        return parseVideosFromElements(rankingElements)
    }
}