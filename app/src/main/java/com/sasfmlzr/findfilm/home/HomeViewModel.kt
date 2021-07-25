package com.sasfmlzr.findfilm.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sasfmlzr.findfilm.Graph
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import com.sasfmlzr.findfilm.request.FindFilmRepository
import com.sasfmlzr.findfilm.request.MovieSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val findFilmRepository: FindFilmRepository = Graph.findFilmRepository
) : ViewModel() {

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeViewState())

    private val refreshing = MutableStateFlow(false)

    val state: StateFlow<HomeViewState>
        get() = _state

    val movies: Flow<PagingData<DiscoverMovieRequest.Result>> = Pager(PagingConfig(pageSize = 20)) {
        MovieSource(findFilmRepository)
    }.flow

    fun refresh(page: Int, force: Boolean) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                findFilmRepository.getNumThreadsFromCatalog(page, force)
            }.onFailure {
                refreshing.value = false
                _state.value = HomeViewState(errorMessage = "Service are unavailable")
            }.onSuccess {
                refreshing.value = false
                println()
            }
            // TODO: look at result of runCatching and show any errors

            refreshing.value = false
        }
    }
}

data class HomeViewState(
    val featuredPodcasts: List<DiscoverMovieRequest> = emptyList(),
    val refreshing: Boolean = false,
    val errorMessage: String? = null
)
