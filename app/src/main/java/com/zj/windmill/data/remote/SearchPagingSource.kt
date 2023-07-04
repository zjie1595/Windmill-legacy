package com.zj.windmill.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zj.windmill.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 搜索分页源
 * @author zj
 * @date 2023/07/04
 * @constructor 创建[SearchPagingSource]
 * @param [keyword] 关键字
 * @param [searchPageParser] 搜索页面解析器
 */
class SearchPagingSource(
    private val keyword: String,
    private val searchPageParser: SearchPageParser
) : PagingSource<Int, Video>() {

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val page = params.key ?: 0
            val videos = withContext(Dispatchers.IO) {
                searchPageParser.parseSearchPage(keyword, page)
            }
            val nextKey = if (videos.isEmpty()) null else page + 1
            LoadResult.Page(
                data = videos,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e.cause ?: Throwable("unknown error"))
        }
    }
}