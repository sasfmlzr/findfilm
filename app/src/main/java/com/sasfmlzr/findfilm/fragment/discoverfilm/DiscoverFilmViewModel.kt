package com.sasfmlzr.findfilm.fragment.discoverfilm

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.sasfmlzr.findfilm.adapter.DiscoverRecyclerAdapter
import com.sasfmlzr.findfilm.model.RetrofitSingleton
import com.sasfmlzr.findfilm.model.SystemSettings
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import com.sasfmlzr.findfilm.request.FindFilmApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DiscoverFilmViewModel internal constructor(
    private val filmSelectedListener: DiscoverFilmFragment.OnFilmSelectedListener?
) : BaseObservable() {

    @Bindable
    var recyclerViewAdapter: RecyclerView.Adapter<*>? = null
        private set(recyclerViewAdapter) {
            field = recyclerViewAdapter
            notifyPropertyChanged(BR.recyclerViewAdapter)
        }
    private var querySearch: String? = null
    private var countLoadedPages = 1
    private var isFirstList = true

    fun runRequest(querySearch: String?) {
        isFirstList = true
        this.querySearch = querySearch
        if (querySearch == null) {
            runRequestFilm(1, filmListListener())
        } else {
            val callback =
                object : DiscoverFilmFragment.SearchCallback {
                    override fun isFind(filmList: List<DiscoverMovieRequest.Result>) {
                        setAdapterDiscoverFilm(filmList)
                    }
                }
            runSearchRequestFilm(querySearch, callback)
        }
    }

    fun runSearchRequestFilm(query: String?, callback: DiscoverFilmFragment.SearchCallback) {
        val findFilmApi: FindFilmApi = RetrofitSingleton.findFilmApi!!
        findFilmApi.getSearchMovie(SystemSettings.API_KEY, SystemSettings.LANGUAGE, query, 1)
            .enqueue(object : Callback<DiscoverMovieRequest?> {
                override fun onResponse(
                    call: Call<DiscoverMovieRequest?>,
                    response: Response<DiscoverMovieRequest?>
                ) {
                    val discoverMovieRequest: DiscoverMovieRequest? = response.body()
                    callback.isFind(discoverMovieRequest?.results!!)
                }

                override fun onFailure(call: Call<DiscoverMovieRequest?>, t: Throwable) {}
            })
    }

    private fun runRequestFilm(
        countLoadedPages: Int,
        callback: DiscoverFilmFragment.FilmListComplete
    ) {
        val findFilmApi: FindFilmApi = RetrofitSingleton.findFilmApi!!
        findFilmApi.getDiscoverMovie(
            SystemSettings.API_KEY,
            SystemSettings.LANGUAGE,
            countLoadedPages
        )
            .enqueue(object : Callback<DiscoverMovieRequest> {
                override fun onResponse(
                    call: Call<DiscoverMovieRequest>,
                    response: Response<DiscoverMovieRequest>
                ) {
                    callback.isCompleted(response.body()?.results!!)
                }

                override fun onFailure(call: Call<DiscoverMovieRequest>, t: Throwable) {}
            })
    }

    private fun filmListListener(): DiscoverFilmFragment.FilmListComplete {
        return object : DiscoverFilmFragment.FilmListComplete {
            override fun isCompleted(filmList: List<DiscoverMovieRequest.Result>) {
                setAdapterDiscoverFilm(filmList)
                countLoadedPages++
            }
        }
    }

    private fun setAdapterDiscoverFilm(filmList: List<DiscoverMovieRequest.Result>) {
        if (isFirstList) {
            val callback = object : DiscoverFilmFragment.RecyclerElementEnded {
                override val isEnded: Unit
                    get() = if (querySearch == null) {
                        runRequestFilm(countLoadedPages, filmListListener())
                    } else Unit

            }
            val adapter: RecyclerView.Adapter<*> =
                DiscoverRecyclerAdapter(filmList.toMutableList(), filmSelectedListener!!, callback)
            recyclerViewAdapter = adapter
            isFirstList = false
        } else {
            (Objects.requireNonNull(
                recyclerViewAdapter
            ) as DiscoverRecyclerAdapter)
                .addElements(filmList)
        }
    }
}
