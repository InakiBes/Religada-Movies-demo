package com.religada.moviesdemo.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.religada.moviesdemo.BuildConfig
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import com.religada.moviesdemo.databinding.ItemMovieBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.navigator.Screens
import com.religada.moviesdemo.utils.log
import com.religada.moviesdemo.utils.setImage

class RecyclerViewAdapterFavorites(
    private val items: MutableList<FavoriteMovieRoom>,
    private val appNavigator: AppNavigator,
    private val makeFavorite: (movieId: Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterFavorites.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return ListViewHolder(view, appNavigator, makeFavorite)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setUpdatedData(items: List<FavoriteMovieRoom>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ListViewHolder(
        private val view: View,
        private val appNavigator: AppNavigator,
        private val makeFavorite: (movieId: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val binding = ItemMovieBinding.bind(view)

        fun bind(data: FavoriteMovieRoom) {
            try {
                val imgUrl = BuildConfig.BASE_URL_IMAGES + data.posterPath
                setImage(view.context, imgUrl, binding.imgMovie)
                binding.tvTitleMovie.text = data.title
                binding.tvDescriptionMovie.text = data.overview
                binding.tvVoteAverage.text = data.voteAverage
                binding.tvVoteCount.text =
                    view.context.getString(R.string.number_of_votes, data.voteCount)
                binding.icFavorite.setImageResource(R.drawable.ic_favorite)
                binding.icFavorite.setOnClickListener {
                    makeFavorite(data.movieId)
                }
                binding.boxItemMovie.setOnClickListener {
                    appNavigator.navigateTo(
                        Screens.MOVIE_DETAIL,
                        bundleOf("movieId" to data.movieId)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                log("Error adding data to adapter: $e")
            }
        }
    }
}

