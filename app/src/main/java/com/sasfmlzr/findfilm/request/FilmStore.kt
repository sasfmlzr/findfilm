package com.sasfmlzr.findfilm.request

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow

class FilmStore {
    var films: Flow<DiscoverMovieRequest.Result>?=null
}