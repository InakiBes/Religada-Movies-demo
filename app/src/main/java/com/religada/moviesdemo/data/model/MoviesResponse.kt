package com.religada.moviesdemo.data.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int = -1,
    @SerializedName("results") val movies: List<MovieResponse>,
)

data class MovieResponse(
    @SerializedName("id") val movieId: Int = -1,
    @SerializedName("overview") val overview: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("vote_average") val voteAverage: String = "",
    @SerializedName("vote_count") val voteCount: String = "",
)
