package com.sasfmlzr.findfilm.fragment.currentfilm

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sasfmlzr.findfilm.R
import com.sasfmlzr.findfilm.databinding.CurrentFilmFragmentBinding
import java.util.*

class CurrentFilmFragment : Fragment() {

    private lateinit var viewModel: CurrentFilmViewModel
    private lateinit var binding: CurrentFilmFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = CurrentFilmViewModel()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.current_film_fragment, container, false)
        binding.viewmodel = viewModel
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentMovieField.observe(viewLifecycleOwner) {
            binding.collapsingToolbar.title = it.title

            binding.voteAverage.text = it.voteAverage.toString()
            binding.releaseDate.text = it.releaseDate
            binding.descriptionCurrentFilm.text = it.overview
        }

        viewModel.poster.observe(viewLifecycleOwner) {
            binding.currentFilmImageView.setImageBitmap(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.current_film_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        if (arguments != null) {
            viewModel.start(requireArguments().getInt(ARGUMENT_FILM_ID))
        }
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity?
        setHasOptionsMenu(true)
        if (activity != null) {
            activity.setSupportActionBar(binding.currentFilmToolbar)
            binding.currentFilmToolbar.setNavigationOnClickListener { activity.onBackPressed() }
            Objects.requireNonNull(activity.supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    companion object {
        const val ARGUMENT_FILM_ID = "idFilm"
        fun newInstance(idFilm: Int): CurrentFilmFragment {
            val args = Bundle()
            val fragment = CurrentFilmFragment()
            args.putInt(ARGUMENT_FILM_ID, idFilm)
            fragment.arguments = args
            return fragment
        }
    }
}