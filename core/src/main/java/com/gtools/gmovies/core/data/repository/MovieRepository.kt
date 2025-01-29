package com.gtools.gmovies.core.data.repository

import com.gtools.gmovies.core.data.NetworkBoundResource
import com.gtools.gmovies.core.data.Resource
import com.gtools.gmovies.core.data.source.local.LocalDataSource
import com.gtools.gmovies.core.data.source.remote.ApiResponse
import com.gtools.gmovies.core.data.source.remote.RemoteDataSource
import com.gtools.gmovies.core.data.source.remote.response.MovieResponse
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.core.domain.repository.IMovieRepository
import com.gtools.gmovies.core.utils.AppExecutors
import com.gtools.gmovies.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {
    override fun getAllMovies(sort: String): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovies(sort).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovies()
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapMovieResponsesToEntities(data)
                localDataSource.insertMovies(movieList)
            }
        }.asFlow()

    override fun getSearchMovies(search: String): Flow<List<Movie>> {
        return localDataSource.getMovieSearch(search).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getFavoriteMovies(sort: String): Flow<List<Movie>> {
        return localDataSource.getAllFavoriteMovies(sort).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setMovieFavorite(movie: Movie, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute { localDataSource.setMovieFavorite(movieEntity, state) }
    }
}