package com.zj.windmill.data.remote

data class FilterWrapper(
    var region: String? = null,
    var genre: String? = null,
    var letter: String? = null,
    var year: String? = null,
    var season: String? = null,
    var status: String? = null,
    var label: String? = null,
    var order: String? = null
) {


    /**
     * 过滤url
     *
     * 使用Map来存储过滤参数，这可以使我们更简洁地构建URL的查询参数部分。

    使用buildString来构建URL字符串，这是一个简洁而高效的方式。

    使用takeIf来简化条件检查。如果值不符合条件，它会返回null，这可以避免添加不必要的查询参数。

    使用joinToString来简洁地将查询参数连接为一个字符串。
     * @param [host] 主机
     * @return [String]
     */
    fun filterUrl(host: String): String {
        val parameters = mutableMapOf<String, String?>()

        parameters["region"] = region.takeIf { it != "全部" }
        parameters["genre"] = genre.takeIf { it != "全部" }
        parameters["letter"] = letter.takeIf { it != "全部" }
        parameters["year"] = year.takeIf { it != "全部" }
        parameters["season"] = season.takeIf { it != "全部" }
        parameters["status"] = status.takeIf { it != "全部" }
        parameters["label"] = label.takeIf { it != "全部" }
        parameters["order"] = order.takeIf { it != "全部" && it != "更新时间" }

        return buildString {
            append("$host/list/")
            parameters.entries
                .filter { it.value != null }
                .joinToString(separator = "&", prefix = "?") { "${it.key}=${it.value}" }
                .let { append(it) }
        }
    }
}