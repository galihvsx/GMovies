package com.gtools.gmovies.core.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.gtools.gmovies.core.R
import com.gtools.gmovies.core.databinding.ItemCardListBinding
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.utils.DiffUtils
import java.util.Locale

class MoviesAdapter(

) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var listData = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setData(newListData: List<Movie>?) {
        if (newListData == null) return
        val diffUtilCallback = DiffUtils(listData, newListData)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemCardListBinding =
            ItemCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemCardListBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class MovieViewHolder(private val binding: ItemCardListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
        Log.d("MovieViewHolder", "MovieViewHolder: $movie")
            with(binding) {
                title.text = movie.title
                date.text = movie.releaseDate
                language.text = movie.originalLanguage

                userScore.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)

                Glide.with(itemView.context)
                    .load(itemView.context.getString(R.string.baseUrlImage, movie.posterPath))
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }
                    })
                    .into(poster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}