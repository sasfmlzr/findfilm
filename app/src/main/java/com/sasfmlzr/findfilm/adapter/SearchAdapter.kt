package com.sasfmlzr.findfilm.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cursoradapter.widget.CursorAdapter
import androidx.databinding.DataBindingUtil
import com.sasfmlzr.findfilm.R
import com.sasfmlzr.findfilm.databinding.SearchItemBinding
import com.sasfmlzr.findfilm.fragment.discoverfilm.DiscoverFilmFragment.OnFilmSelectedListener
import com.sasfmlzr.findfilm.request.DiscoverMovieRequest

class SearchAdapter(
    context: Context?,
    cursor: Cursor?,
    private val filmList: List<DiscoverMovieRequest.Result>?,
    private val filmSelectedListener: OnFilmSelectedListener
) : CursorAdapter(context, cursor, false) {
    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val binding: SearchItemBinding = DataBindingUtil.bind(view)!!
        val holder = ViewHolder(binding)
        if (filmList != null) {
            holder.binding.itemSearchTitle.text = filmList[cursor.position].title
        }
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val inflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        return inflater.inflate(R.layout.search_item, parent, false)
    }

    private inner class ViewHolder(val binding: SearchItemBinding) :
        View.OnClickListener {
        val position = cursor.position
        override fun onClick(v: View) {
            filmSelectedListener.filmClicked(filmList!![position].id)
        }

        init {
            binding.itemSearchTitle.setOnClickListener(this)
        }
    }
}
