package com.religada.moviesdemo.data.repository

import com.religada.moviesdemo.data.local.FavoriteMovieDao
import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MovieRepositoryLocal @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
){

    fun getAllFavorites(): List<FavoriteMovieRoom>{
        return runBlocking(Dispatchers.IO) {
            favoriteMovieDao.getAllFavorites()
        }
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return (favoriteMovieDao.findFavoriteById(movieId) != null)
    }

    suspend fun addFavorite(movie: FavoriteMovieRoom){
        favoriteMovieDao.addFavorite(movie)
    }

    fun deleteFavoriteById(movieId: Int, onResponse:()-> Unit){
        runBlocking(Dispatchers.IO) {
            favoriteMovieDao.deleteFavorite(movieId)
            onResponse()
        }
    }
}