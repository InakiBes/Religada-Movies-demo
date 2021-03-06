package com.religada.moviesdemo.di

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.religada.moviesdemo.data.local.AppRoomDatabase
import com.religada.moviesdemo.data.local.FavoriteMovieDao
import com.religada.moviesdemo.notifications.TokenManager
import com.religada.moviesdemo.utils.EncryptPrefManager
import com.religada.moviesdemo.utils.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppRoomDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppRoomDatabase::class.java,
            "movies_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteMovieDao(appRoomDatabase: AppRoomDatabase): FavoriteMovieDao {
        return appRoomDatabase.favoriteMovieDao()
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context,
        encryptPref: EncryptPrefManager,
        prefManager: PrefManager
    ): TokenManager {
        return TokenManager(context, encryptPref, prefManager)
    }

}