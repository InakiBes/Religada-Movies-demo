package com.religada.moviesdemo.data.remote

import com.google.gson.GsonBuilder
import com.religada.moviesdemo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiConnection{

    //jsconConverter
    private val jsonConverter = GsonBuilder()
        .setLenient()
        .disableHtmlEscaping()
        .create()

    /****NO AUTH RETROFIT***/
    //Client No Auth
    private val clientNoAuth: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    //Retrofit No Auth
    private fun getRetroInstanceNoAuth(): Retrofit {
        return Retrofit.Builder()
            .client(clientNoAuth)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(jsonConverter))
            .build()
    }

    val RETRO_SERVICE_NO_AUTH: ApiCall by lazy {
        getRetroInstanceNoAuth()
            .create(ApiCall::class.java)
    }
}
