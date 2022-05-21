package com.religada.moviesdemo.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.religada.moviesdemo.BuildConfig
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.data.repository.MovieRepositoryLocal
import com.religada.moviesdemo.databinding.ItemMovieBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.navigator.Screens

import com.religada.moviesdemo.utils.log
import com.religada.moviesdemo.utils.setImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecyclerViewAdapterMovies(
    private val items: MutableList<MovieResponse>,
    private val appNavigator: AppNavigator,
    private val repositoryLocal: MovieRepositoryLocal,
    private val makeFavorite: (movie: MovieResponse, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterMovies.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return ListViewHolder(view, appNavigator, repositoryLocal, makeFavorite)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setUpdatedData(items: List<MovieResponse>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ListViewHolder(
        private val view: View,
        private val appNavigator: AppNavigator,
        private val repositoryLocal: MovieRepositoryLocal,
        private val makeFavorite: (movie: MovieResponse, isFavorite: Boolean) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val binding = ItemMovieBinding.bind(view)
        private var isFavorite = false

        fun bind(data: MovieResponse) {
            try {
                val imgUrl = BuildConfig.BASE_URL_IMAGES + data.posterPath
                setImage(view.context, imgUrl, binding.imgMovie)
                binding.tvTitleMovie.text = data.title
                binding.tvDescriptionMovie.text = data.overview
                binding.tvVoteAverage.text = data.voteAverage
                binding.tvVoteCount.text = view.context.getString(R.string.number_of_votes, data.voteCount)
                setFavoriteIcon(isFavorite(data.movieId))
                binding.icFavorite.setOnClickListener{
                    makeFavorite(data, isFavorite)
                    setFavoriteIcon(!isFavorite)
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

        private fun isFavorite(movieId: Int): Boolean = runBlocking {
            repositoryLocal.isFavorite(movieId)
        }

        private fun setFavoriteIcon(isFavorite: Boolean) {
            this.isFavorite = isFavorite
            if(isFavorite)
                binding.icFavorite.setImageResource(R.drawable.ic_favorite)
            else
                binding.icFavorite.setImageResource(R.drawable.ic_no_favorite)
        }
    }
}

