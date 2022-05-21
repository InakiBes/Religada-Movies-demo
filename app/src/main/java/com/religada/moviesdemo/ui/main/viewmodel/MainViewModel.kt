package com.religada.moviesdemo.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.data.repository.MovieRepository
import com.religada.moviesdemo.widget.OnResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var movieRepository: MovieRepository

    var isLoading = false
    var isLastPage = false
    var page = 1

    fun getPopularMovies(onResponse:(List<MovieResponse>)-> Unit){
        movieRepository.getPopularMovies(page, getLanguage()){ response ->
            when (response) {
                is OnResult.Success -> onResponse(response.data)
                is OnResult.Error -> onResponse(emptyList<MovieResponse>())
            }
        }
    }

    private fun getLanguage() = if (Locale.getDefault().toString().subSequence(0, 2) == "en") "en-us" else "es-es"
}