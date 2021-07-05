package com.sasfmlzr.findfilm.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasfmlzr.findfilm.Graph
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import com.sasfmlzr.findfilm.request.FindFilmRepository
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


    private fun refresh(page: Int, force: Boolean) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                findFilmRepository.getNumThreadsFromCatalog(page, force)
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
