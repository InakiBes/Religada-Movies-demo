package com.religada.moviesdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovieRoom::class], version = 1, exportSchema = false)

abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao() : FavoriteMovieDao
}