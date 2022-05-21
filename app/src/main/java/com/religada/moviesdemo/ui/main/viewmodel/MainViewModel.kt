package com.religada.moviesdemo.ui.main.viewmodel

import android.graphics.Movie
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import com.religada.moviesdemo.data.mapper.MovieMapper
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.data.repository.MovieRepositoryLocal
import com.religada.moviesdemo.data.repository.MovieRepositoryRemote
import com.religada.moviesdemo.widget.OnResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var movieRepositoryRemote: MovieRepositoryRemote
    @Inject
    lateinit var movieRepositoryLocal: MovieRepositoryLocal
    @Inject
    lateinit var mapper: MovieMapper

    var isLoading = false
    var isLastPage = false
    var page = 1

    private val mFavorites = MutableLiveData<List<FavoriteMovieRoom>>()
    val favorites: LiveData<List<FavoriteMovieRoom>>
        get() = mFavorites

    fun getPopularMovies(onResponse:(List<MovieResponse>)-> Unit){
        movieRepositoryRemote.getPopularMovies(page, getLanguage()){ response ->
            when (response) {
                is OnResult.Success -> onResponse(response.data)
                is OnResult.Error -> onResponse(emptyList<MovieResponse>())
            }
        }
    }

    private fun getLanguage() = if (Locale.getDefault().toString().subSequence(0, 2) == "en") "en-us" else "es-es"

    fun isFavorite(movieId: Int, onResponse:(Boolean)-> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            onResponse(movieRepositoryLocal.isFavorite(movieId))
        }
    }

    fun makeFavorite(movie: MovieResponse, isFavorite: Boolean) {
        if(isFavorite)
            deleteFavoriteById(movie.movieId)
        else
            addFavorite(movie)
    }

    private fun addFavorite(movie: MovieResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepositoryLocal.addFavorite(
                mapper.mapMovieResponseToFavoriteMovieRoom(movie)
            )
        }
    }

    private fun deleteFavoriteById(movieId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepositoryLocal.deleteFavoriteById(movieId)
        }
    }

    fun getAllFavorites(onResponse:(List<FavoriteMovieRoom>)-> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            onResponse(
                movieRepositoryLocal.getAllFavorites()
            )
        }
    }


}