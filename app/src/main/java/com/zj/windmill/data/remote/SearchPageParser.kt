package com.zj.windmill.data.remote

import com.zj.windmill.model.Video
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import javax.inject.Inject

class SearchPageParser @Inject constructor(
    okHttpClient: OkHttpClient
) : PageParser(okHttpClient) {

    /**
     * 解析搜索页面
     * @param [keyword] 关键字
     * @param [page] 页面 从0开始
     * @return [List<Video>]
     */
    fun parseSearchPage(keyword: String, page: Int): List<Video> {
        val searchPageUrl = "${host}/s_all?kw=${keyword}&pagesize=24&pageindex=${page}"
        val document = fetchDocument(searchPageUrl) ?: return emptyList()
        val searchResultElements =
            document.select("body > div:nth-child(4) > div.fire.l > div.lpic > ul > li")
        return parseVideosFromElements(searchResultElements)
    }
}