package com.gtools.gmovies.core.data.source.remote

import com.gtools.gmovies.core.data.source.remote.response.ListMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
    ): ListMovieResponse
}