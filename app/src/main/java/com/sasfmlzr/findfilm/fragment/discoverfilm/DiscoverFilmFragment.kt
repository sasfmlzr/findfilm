package com.sasfmlzr.findfilm.fragment.discoverfilm

import android.content.Context
import android.database.MatrixCursor
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sasfmlzr.findfilm.R
import com.sasfmlzr.findfilm.adapter.SearchAdapter
import com.sasfmlzr.findfilm.adapter.VerticalItemDecoration
import com.sasfmlzr.findfilm.databinding.DiscoverFragmentBinding
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import java.util.*

class DiscoverFilmFragment : Fragment() {
    private var querySearch: String? = null
    private var viewModel: DiscoverFilmViewModel? = null
    private var viewDataBinding: DiscoverFragmentBinding? = null
    var filmSelectedListener: OnFilmSelectedListener? = null
    var searchView: SearchView? = null
    var savedState: Bundle? = null
    private var timer: Timer? = null
    private var menu: Menu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            querySearch = requireArguments().getString(QUERY_SEARCH_ARGS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DiscoverFragmentBinding.bind(inflater.inflate(R.layout.discover_fragment, container, false))
        setHasOptionsMenu(true)
        savedState = null
        viewModel = DiscoverFilmViewModel(filmSelectedListener)
       viewDataBinding!!.viewmodel = viewModel
        loadRecyclerFilmView()
        viewModel!!.runRequest(querySearch)
        return viewDataBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        savedState = saveState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchView != null) {
            outState.putString("currentSearchQuery", searchView!!.query.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        filmSelectedListener = try {
            parentFragment as OnFilmSelectedListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$context OnFilmSelectedListener not attached")
        }
    }

    override fun onDetach() {
        super.onDetach()
        filmSelectedListener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filmSelectedListener!!.filmSearched(query)
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                loadHistory(query)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    interface RecyclerElementEnded {
        val isEnded: Unit
    }

    interface SearchCallback {
        fun isFind(filmList: List<DiscoverMovieRequest.Result>)
    }

    interface OnFilmSelectedListener {
        fun filmClicked(idFilm: Int)
        fun filmSearched(query: String?)
    }

    interface FilmListComplete {
        fun isCompleted(filmList: List<DiscoverMovieRequest.Result>)
    }

    private fun loadRecyclerFilmView() {
        viewDataBinding!!.discoverFilmList.layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding!!.discoverFilmList.addItemDecoration(VerticalItemDecoration(50))
    }

    private fun saveState(): Bundle {
        val state = Bundle()
        if (searchView != null) {
            state.putString("currentSearchQuery", searchView!!.query.toString())
        }
        return state
    }

    private fun loadHistory(query: String) {
        val columns = arrayOf("_id", "text")
        val temp = arrayOf<Any>(0, "default")
        val search = menu!!.findItem(R.id.search).actionView as SearchView
        val cursor = MatrixCursor(columns)
        if (query.isEmpty()) {
            search.suggestionsAdapter = SearchAdapter(
                context,
                cursor,
                null,
                filmSelectedListener!!
            )
            return
        }
        if (query.length > 2) {
            val callback = object: SearchCallback{
                override fun isFind(filmList: List<DiscoverMovieRequest.Result>) {
                    for (i in filmList.indices) {
                        temp[0] = i
                        temp[1] = filmList[i]
                        cursor.addRow(temp)
                    }
                    search.suggestionsAdapter = SearchAdapter(
                        context,
                        cursor,
                        filmList,
                        filmSelectedListener!!
                    )
                }
            }

            val handler = Handler()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    handler.post {
                        viewModel!!.runSearchRequestFilm(query, callback)
                        timer = null
                    }
                }
            }
            if (timer != null) {
                timer!!.cancel()
            }
            timer = Timer()
            timer!!.schedule(timerTask, 2000)
        }
    }

    companion object {
        private const val QUERY_SEARCH_ARGS = "querySearch"
        fun newInstance(querySearch: String?): DiscoverFilmFragment {
            val args = Bundle()
            args.putString(QUERY_SEARCH_ARGS, querySearch)
            val fragment = DiscoverFilmFragment()
            fragment.arguments = args
            return fragment
        }
    }
}