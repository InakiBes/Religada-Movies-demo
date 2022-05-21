package com.religada.moviesdemo.data.repository

import com.religada.moviesdemo.data.local.FavoriteMovieDao
import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import javax.inject.Inject

class MovieRepositoryLocal @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
){
    suspend fun getAllFavorites(): List<FavoriteMovieRoom>{
        return favoriteMovieDao.getAllFavorites()
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return (favoriteMovieDao.findFavoriteById(movieId) != null)
    }

    suspend fun addFavorite(movie: FavoriteMovieRoom){
        favoriteMovieDao.addFavorite(movie)
    }

    suspend fun deleteFavoriteById(movieId: Int){
        favoriteMovieDao.deleteFavorite(movieId)
    }
}