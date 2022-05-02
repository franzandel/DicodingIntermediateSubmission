package com.franzandel.dicodingintermediatesubmission.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.franzandel.dicodingintermediatesubmission.data.consts.PaginationConst
import com.franzandel.dicodingintermediatesubmission.data.model.StoryResponse
import com.franzandel.dicodingintermediatesubmission.data.service.HomeService
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Franz Andel
 * on 02 May 2022.
 */

class HomePagingSource(
    private val service: HomeService,
    private val token: String
) : PagingSource<Int, StoryResponse>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getStories("Bearer $token", position, params.loadSize)
            val stories = response.listStory
            val nextKey = if (stories.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / PaginationConst.NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = stories,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? =
    // We need to get the previous key (or next key if previous is null) of the page
    // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
