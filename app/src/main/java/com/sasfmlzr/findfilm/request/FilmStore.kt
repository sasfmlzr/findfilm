package com.sasfmlzr.findfilm.request

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class FilmStore {
    var films: Flow<DiscoverMovieRequest>?=null
}