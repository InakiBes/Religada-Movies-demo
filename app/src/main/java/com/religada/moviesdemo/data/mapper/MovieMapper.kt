package com.religada.moviesdemo.data.mapper

import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import com.religada.moviesdemo.data.model.MovieResponse
import javax.inject.Inject

class MovieMapper @Inject constructor(){

    fun mapMovieResponseToFavoriteMovieRoom(mr: MovieResponse): FavoriteMovieRoom{
        return FavoriteMovieRoom(
            mr.movieId,
            mr.overview,
            mr.popularity,
            mr.posterPath,
            mr.releaseDate,
            mr.title,
            mr.voteAverage,
            mr.voteCount
        )
    }
}