package com.sasfmlzr.findfilm.request

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sasfmlzr.findfilm.successOr

class MovieSource(
    private val findFilmRepository: FindFilmRepository
) : PagingSource<Int, DiscoverMovieRequest.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiscoverMovieRequest.Result> {
        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = findFilmRepository.getNumThreadsFromCatalog(nextPage)
            val data = movieListResponse.successOr(DiscoverMovieRequest())
            println("Loading data")
            LoadResult.Page(
                data = data.results!!,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = data.page+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DiscoverMovieRequest.Result>): Int? {
        TODO("Not yet implemented")
    }
}