package com.gtools.gmovies.core.di

import androidx.room.Room
import com.gtools.gmovies.core.data.repository.MovieRepository
import com.gtools.gmovies.core.data.source.local.LocalDataSource
import com.gtools.gmovies.core.data.source.local.room.MovieDatabase
import com.gtools.gmovies.core.data.source.remote.ApiService
import com.gtools.gmovies.core.data.source.remote.RemoteDataSource
import com.gtools.gmovies.core.domain.repository.IMovieRepository
import com.gtools.gmovies.core.utils.AppExecutors
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import net.sqlcipher.database.SQLiteDatabase
import okhttp3.CertificatePinner


val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("rahasia".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "Movies.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/k1Hdw5sdSn5kh/gemLVSQD/P4i4IBQEY1tW4WNxh9XM=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/discover/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMovieRepository> { MovieRepository(get(), get(), get()) }
}