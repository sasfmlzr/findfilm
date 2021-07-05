package com.sasfmlzr.findfilm.request

import com.sasfmlzr.findfilm.model.SystemSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FindFilmRepository(
    private val api: FindFilmComposeApi,
    private val filmStore: FilmStore,
    mainDispatcher: CoroutineDispatcher
) {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)

    suspend fun getNumThreadsFromCatalog(page: Int, force: Boolean) {
        if (refreshingJob?.isActive == true) {
            refreshingJob?.join()
        } else if (force) {
            refreshingJob = scope.launch {
                // Now fetch the podcasts, and add each to each store
                filmStore.films = flow {
                    emit(
                        api.getDiscoverMovie(
                            SystemSettings.API_KEY,
                            SystemSettings.LANGUAGE, page
                        )
                    )
                }
            }
        }
    }
}
