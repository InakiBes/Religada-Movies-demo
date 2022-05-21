package com.religada.moviesdemo.data.remote

import com.religada.moviesdemo.data.model.MoviesResponse
import com.religada.moviesdemo.data.model.StandardResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCall {

    @POST("firebase-token")
    suspend fun sendFirebaseTokenToServer(
        @Body params: RequestBody
    ): Response<StandardResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String,
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun getMoviesByKeyword(
        @Query("query") key: String,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String,
    ): Response<MoviesResponse>
}