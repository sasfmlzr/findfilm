package com.sasfmlzr.findfilm.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sasfmlzr.findfilm.R
import com.sasfmlzr.findfilm.databinding.DiscoverFilmItemBinding
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment.OnFilmSelectedListener
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment.RecyclerElementEnded
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target

class DiscoverRecyclerAdapter(
    private val filmList: MutableList<DiscoverMovieRequest.Result>,
    private val filmSelectedListener: OnFilmSelectedListener,
    private val elementEndedCallback: RecyclerElementEnded
) : RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder>() {

    private var viewModel: DiscoverItemViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.discover_film_item, parent, false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFilm = filmList[position]
        viewModel = DiscoverItemViewModel()
        holder.binding.viewmodel = viewModel
        val binding = holder.binding
        binding.nameFilm.text = currentFilm.title
        binding.scoreFilm.text = currentFilm.voteAverage.toString()
        viewModel?.loadBitmap(currentFilm, holder.target)
        if (position == filmList.size - 1) {
            elementEndedCallback.isEnded
        }
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    fun addElements(filmList: List<DiscoverMovieRequest.Result>?) {
        this.filmList.addAll(filmList!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(val binding: DiscoverFilmItemBinding) :
        RecyclerView.ViewHolder(
            binding.itemCardView
        ), View.OnClickListener {
        val target: Target
        override fun onClick(v: View) {
            filmSelectedListener.filmClicked(filmList[adapterPosition].id)
        }

        init {
            target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                    binding.previewFilmImageView.setImageBitmap(bitmap)
                    binding.progressBarLoaderImage.visibility = View.INVISIBLE
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }
            itemView.setOnClickListener(this)
        }
    }
}
