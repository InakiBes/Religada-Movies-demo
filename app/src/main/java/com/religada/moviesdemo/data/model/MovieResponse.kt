package com.religada.moviesdemo.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("movie_id") val movieId: Int = -1,

)

