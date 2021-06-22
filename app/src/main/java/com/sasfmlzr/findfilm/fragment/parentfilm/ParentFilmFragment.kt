package com.sasfmlzr.findfilm.fragment.parentfilm

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.sasfmlzr.findfilm.R
import com.sasfmlzr.findfilm.databinding.ContainerFragmentBinding
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment.OnFilmSelectedListener

class ParentFilmFragment : Fragment(), OnFilmSelectedListener {
    private var viewDataBinding: ContainerFragmentBinding? = null
    private var query: String? = null
    private var searchedListener: filmClickedListener? = null
    private var myFragmentState: SavedState? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            query = requireArguments().getString("query")
            myFragmentState = requireArguments().getParcelable("fragment")
        }
        if (myFragmentState != null) {
            setInitialSavedState(myFragmentState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.container_fragment, container, false)
        setHasOptionsMenu(true)
        viewDataBinding = ContainerFragmentBinding.bind(view)
        setupToolbar()
        configureBottomNavigation()
        configureTopButtons()
        return viewDataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (myFragmentState == null) {
            replaceChildFragment()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun filmClicked(idFilm: Int) {
        searchedListener!!.isClicked(idFilm)
    }

    override fun filmSearched(query: String?) {
        this.query = query
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction()
            .replace(
                R.id.container_child_fragment,
                DiscoverFilmFragment.newInstance(query)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val viewSearch = menu.findItem(R.id.search).actionView
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = viewSearch as SearchView
        assert(searchManager != null)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setIconifiedByDefault(true)
        if (query != null && query!!.isNotEmpty()) {
            searchView.setQuery(query, false)
            searchView.clearFocus()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchedListener = activity as filmClickedListener?
    }

    override fun onDetach() {
        searchedListener = null
        super.onDetach()
    }

    override fun onDestroyView() {
        assert(fragmentManager != null)
        myFragmentState = requireFragmentManager().saveFragmentInstanceState(this)
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("query", query)
        outState.putParcelable("fragment", myFragmentState)
        super.onSaveInstanceState(outState)
    }

    interface filmClickedListener {
        fun isClicked(idFilm: Int)
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity?
        val toolbar: Toolbar = viewDataBinding!!.root.findViewById(R.id.toolbar)
        if (activity != null) {
            toolbar.title = "TMDB"
            activity.setSupportActionBar(toolbar)
        }
    }

    private fun replaceChildFragment() {
        val childFragment = DiscoverFilmFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.container_child_fragment, childFragment)
            .commit()
    }

    private fun configureTopButtons() {
        val buttonNow: MaterialButton = viewDataBinding!!.root.findViewById(R.id.button_now)
        val buttonSoon: MaterialButton = viewDataBinding!!.root.findViewById(R.id.button_soon)
        buttonNow.background = configureLeftButton(255)
        buttonSoon.background = configureRightButton(128)
        buttonNow.setOnClickListener { item: View? ->
            Toast.makeText(context, "Now clicked", Toast.LENGTH_SHORT).show()
            buttonNow.background = configureLeftButton(255)
            buttonSoon.background = configureRightButton(128)
            buttonNow.setTextColor(resources.getColor(R.color.colorBlack))
            buttonSoon.setTextColor(resources.getColor(R.color.colorWhite))
        }
        buttonSoon.setOnClickListener { item: View? ->
            Toast.makeText(context, "Soon clicked", Toast.LENGTH_SHORT).show()
            buttonNow.background = configureLeftButton(128)
            buttonSoon.background = configureRightButton(255)
            buttonSoon.setTextColor(resources.getColor(R.color.colorBlack))
            buttonNow.setTextColor(resources.getColor(R.color.colorWhite))
        }
    }

    private fun configureLeftButton(
        @IntRange(
            from = 0L,
            to = 255L
        ) opacity: Int
    ): MaterialShapeDrawable {
        val shape = ShapeAppearanceModel.builder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 40f)
            .setTopLeftCorner(CornerFamily.ROUNDED, 40f)
            .build()
        val leftRoundedMaterialShape = MaterialShapeDrawable(shape)
        leftRoundedMaterialShape.setTint(COLOR_PRIMARY)
        leftRoundedMaterialShape.alpha = opacity
        return leftRoundedMaterialShape
    }

    private fun configureRightButton(
        @IntRange(
            from = 0L,
            to = 255L
        ) opacity: Int
    ): MaterialShapeDrawable {
        val shape = ShapeAppearanceModel.builder()
            .setBottomRightCorner(CornerFamily.ROUNDED, 40f)
            .setTopRightCorner(CornerFamily.ROUNDED, 40f)
            .build()
        val rightRoundedMaterialShape = MaterialShapeDrawable(shape)
        rightRoundedMaterialShape.setTint(COLOR_PRIMARY)
        rightRoundedMaterialShape.alpha = opacity
        return rightRoundedMaterialShape
    }

    private fun configureBottomNavigation() {
        val bottomNavigationView: BottomNavigationView =
            viewDataBinding!!.root.findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_child -> Toast.makeText(
                    context,
                    "Child pushed", Toast.LENGTH_SHORT
                ).show()
                R.id.action_favorites -> Toast.makeText(
                    context,
                    "Favorites pushed", Toast.LENGTH_SHORT
                ).show()
                R.id.action_invalid -> Toast.makeText(
                    context,
                    "Invalid pushed", Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    companion object {

        private const val COLOR_PRIMARY = -0x151512
    }
}