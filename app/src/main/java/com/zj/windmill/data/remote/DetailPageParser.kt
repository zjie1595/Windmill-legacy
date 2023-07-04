package com.zj.windmill.data.remote

import com.zj.windmill.model.DetailPage
import com.zj.windmill.model.Episode
import com.zj.windmill.model.Playlist
import okhttp3.OkHttpClient
import javax.inject.Inject

class DetailPageParser @Inject constructor(
    okHttpClient: OkHttpClient
) : PageParser(okHttpClient) {

    /**
     * 解析详细信息页面
     * @param [detailPageUrl] 详细信息页面url
     * @return [DetailPage?]
     */
    fun parseDetailPage(detailPageUrl: String): DetailPage? {
        val document = fetchDocument(detailPageUrl) ?: return null
        val playlists = document.select("#main0 > div > ul").mapIndexed { index, element ->
            val playlistTitle = "播放列表${index}"
            val episodes = element.select("li").map { li ->
                val a = li.select("a")
                val playPageUrl = constructDetailPageUrl(a)
                val title = a.text()
                Episode(title = title, playPageUrl = playPageUrl)
            }
            Playlist(title = playlistTitle, episodes = episodes)
        }
        val defaultPlayIndex = document.select("#DEF_PLAYINDEX").text().toIntOrNull() ?: 0
        return DetailPage(defaultPlayIndex = defaultPlayIndex, playlists = playlists)
    }
}