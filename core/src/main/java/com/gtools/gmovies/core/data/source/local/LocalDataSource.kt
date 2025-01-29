package com.gtools.gmovies.core.data.source.local

import com.gtools.gmovies.core.data.source.local.entity.MovieEntity
import com.gtools.gmovies.core.data.source.local.room.MovieDao
import com.gtools.gmovies.core.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class LocalDataSource(private val mMovieDao: MovieDao) {

    fun getAllMovies(sort: String): Flow<List<MovieEntity>> {
        val query = SortUtils.getSortedQueryMovies(sort)
        return mMovieDao.getMovies(query)
    }

    fun getAllFavoriteMovies(sort: String): Flow<List<MovieEntity>> {
        val query = SortUtils.getSortedQueryFavoriteMovies(sort)
        return mMovieDao.getFavoriteMovies(query)
    }

    fun getMovieSearch(search: String): Flow<List<MovieEntity>> {
        return mMovieDao.getSearchMovies(search)
            .flowOn(Dispatchers.Default)
            .conflate()
    }

    suspend fun insertMovies(movies: List<MovieEntity>) = mMovieDao.insertMovie(movies)

    fun setMovieFavorite(movie: MovieEntity, newState: Boolean) {
        movie.favorite = newState
        mMovieDao.updateFavoriteMovie(movie)
    }
}