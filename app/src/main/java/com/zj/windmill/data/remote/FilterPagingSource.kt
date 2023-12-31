package com.zj.windmill.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zj.windmill.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 过滤器分页源
 * @author zj
 * @date 2023/07/04
 * @constructor 创建[FilterPagingSource]
 * @param [catalogPageParser] 目录页面解析器
 * @param [filterWrapper] 过滤包装
 */
class FilterPagingSource(
    private val catalogPageParser: CatalogPageParser,
    private val filterWrapper: FilterWrapper
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
            val searchResult = withContext(Dispatchers.IO) {
                catalogPageParser.filter(filterWrapper, page)
            } ?: return LoadResult.Error(Throwable("unknown error"))
            val nextKey = if (searchResult.hasMore) page + 1 else null
            LoadResult.Page(
                data = searchResult.videos,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e.cause ?: Throwable("unknown error"))
        }
    }
}