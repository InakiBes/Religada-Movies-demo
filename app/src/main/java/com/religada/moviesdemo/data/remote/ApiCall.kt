package com.religada.moviesdemo.data.remote

import com.religada.moviesdemo.data.model.StandardResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiCall {

    // Firebase token
    @POST("firebase-token")
    suspend  fun sendFirebaseTokenToServer(
        @Body params : RequestBody
    ): Response<StandardResponse>

}