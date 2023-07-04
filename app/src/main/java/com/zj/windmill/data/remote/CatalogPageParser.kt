package com.zj.windmill.data.remote

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.zj.windmill.model.Video
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern
import javax.inject.Inject

class CatalogPageParser @Inject constructor(
    okHttpClient: OkHttpClient
) : PageParser(okHttpClient) {

    fun parseLabelMap(): Map<String, List<String>> {
        val labelMap = mutableMapOf<String, List<String>>()
        try {
            val request =
                Request.Builder().url("https://www.xmfans.me/yxsf/js/yx_catalog.js?ver=156257")
                    .build()
            val script =
                okHttpClient.newCall(request).execute().body?.string() ?: return labelMap
            val pattern = Pattern.compile(".*?_labels = (.*?);.*?")
            val matcher = pattern.matcher(script)
            while (matcher.find()) {
                val group = matcher.group()
                if (group.contains("[")) {
                    val labels = group.split("=")[1].split(";")[0].trim()
                    val type = object : TypeToken<List<String>>() {}.type
                    val labelList: List<String> = GsonUtils.fromJson(labels, type)
                    labelMap[labelList[0]] = labelList.drop(1)
                }
            }
        } catch (e: Exception) {
            Logger.e(e.cause, "Failed to parse label map")
        }
        return labelMap
    }

    /**
     * 根据地区、语言、类别等一个或多个标签过滤筛选视频
     * @param [filterWrapper] 过滤包装
     * @param [page] 页码，从0开始
     * @return [List<Video>]
     */
    fun filter(filterWrapper: FilterWrapper, page: Int): List<Video> {
        val catalogPageUrl = "${filterWrapper.filterUrl(host)}/?pagesize=24&pageindex=${page}"
        val document = fetchDocument(catalogPageUrl) ?: return emptyList()
        val filterResultElements =
            document.select("body > div:nth-child(4) > div.fire.l > div.lpic > ul > li")
        return parseVideosFromElements(filterResultElements)
    }
}