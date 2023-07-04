package com.zj.windmill.data.remote

import com.orhanobut.logger.Logger
import com.zj.windmill.model.Video
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import javax.inject.Inject

open class PageParser @Inject constructor(
    protected val okHttpClient: OkHttpClient
) {

    protected val host = "https://www.dm530w.org"

    protected fun constructImageUrl(imageElements: Elements): String {
        val src = imageElements.attr("src")
        return if (src.startsWith("http")) src else "https:$src"
    }

    protected fun constructDetailPageUrl(anchorElements: Elements): String {
        val href = anchorElements.attr("href")
        return host + href
    }

    protected fun fetchDocument(pageUrl: String): Document? {
        return runCatching {
            val request = Request.Builder()
                .url(pageUrl)
                .build()
            val html = okHttpClient.newCall(request).execute().body?.string() ?: return null
            Jsoup.parse(html)
        }.onFailure {
            Logger.e(it, "Failed to parse page")
        }.getOrNull()
    }

    /**
     * 解析视频元素
     * @param [elements] 元素
     * @return [List<Video>]
     */
    protected fun parseVideosFromElements(elements: Elements): List<Video> {
        return elements.map { element ->
            val anchor = element.select("a")
            val image = anchor.select("img")
            val detailPageUrl = constructDetailPageUrl(anchor)
            val coverImageUrl = constructImageUrl(image)
            val title = image.attr("alt")
            Video(title = title, coverImageUrl = coverImageUrl, detailPageUrl = detailPageUrl)
        }
    }
}