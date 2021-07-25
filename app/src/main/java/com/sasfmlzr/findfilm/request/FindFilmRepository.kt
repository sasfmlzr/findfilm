package com.sasfmlzr.findfilm.request

import com.sasfmlzr.findfilm.Result
import com.sasfmlzr.findfilm.model.SystemSettings
import kotlinx.coroutines.*

class FindFilmRepository(
    private val api: FindFilmComposeApi,
    private val filmStore: FilmStore,
    mainDispatcher: CoroutineDispatcher
) {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)

    suspend fun getNumThreadsFromCatalog(
        page: Int,
        force: Boolean = true
    ): Result<DiscoverMovieRequest> {
        return withContext(Dispatchers.IO) {
            val result: DiscoverMovieRequest = api.getDiscoverMovie(
                SystemSettings.API_KEY,
                SystemSettings.LANGUAGE, page
            )
            Result.Success(result)
        }
    }
}
