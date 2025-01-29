package com.gtools.gmovies.core.domain.repository

import com.gtools.gmovies.core.data.Resource
import com.gtools.gmovies.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

    fun getAllMovies(sort: String): Flow<Resource<List<Movie>>>

    fun getFavoriteMovies(sort: String): Flow<List<Movie>>

    fun getSearchMovies(search: String): Flow<List<Movie>>

    fun setMovieFavorite(movie: Movie, state: Boolean)
}