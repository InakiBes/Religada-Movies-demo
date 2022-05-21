package com.religada.moviesdemo.data.local

import androidx.room.*

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteMovieRoom: FavoriteMovieRoom): Long

    @Query("SELECT * FROM movies_table")
    suspend fun getAllFavorites(): List<FavoriteMovieRoom>

    @Query("DELETE FROM movies_table WHERE movieId LIKE :id")
    suspend fun deleteFavorite(id: Int): Int

    @Query("SELECT * FROM movies_table WHERE movieId LIKE :id LIMIT 1")
    suspend fun findFavoriteById(id: Int): FavoriteMovieRoom?
}