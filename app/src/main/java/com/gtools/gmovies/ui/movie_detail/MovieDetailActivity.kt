package com.gtools.gmovies.ui.movie_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import com.gtools.gmovies.R
import com.gtools.gmovies.core.domain.model.Movie
import com.gtools.gmovies.databinding.ActivityMovieDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModel()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie: Movie? = intent.getParcelableExtra<Movie>("EXTRA_MOVIE")

        setupUi(movie)
        setupAppBar(movie?.title.toString())
    }

    private fun setupAppBar(title: String?) {
        val collapsingToolbar = binding.collapsingToolbar
        collapsingToolbar.expandedTitleTextSize = 0f
        collapsingToolbar.title = title
        // add default back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                this.finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUi(movie: Movie?) {
        val posterHeader: ImageView = binding.posterHeader
        val moviePoster = binding.moviePoster
        val movieTitle = binding.movieTitle
        val movieLang = binding.movieLanguage
        val movieReleaseDate = binding.movieRelease
        val movieOverview = binding.movieOverview
        val movieScore = binding.movieScore
        val moviePopularity = binding.moviePopularity

        val favBtn = binding.favBtn
        val shareBtn = binding.shareBtn

        favBtn.setOnClickListener {
            addToFavorite(movie)
        }



        shareBtn.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder.from(this).apply {
                setType(mimeType)
                setChooserTitle("Share this movie now.")
                setText("Check out this movie: ${movie?.title}")
                startChooser()
            }
        }

        movie?.let { movieDetail: Movie ->
            movieTitle.text = movieDetail.title
            movieOverview.text = movieDetail.overview
            movieReleaseDate.text = movieDetail.releaseDate
            movieLang.text = "Language: ${movieDetail.originalLanguage}"
            movieScore.text = "Score: ${movieDetail.voteAverage}"
            moviePopularity.text = "Popularity: ${movieDetail.popularity}"

            Glide.with(this)
                .load(baseContext.getString(com.gtools.gmovies.core.R.string.baseUrlImage, movieDetail.posterPath))
                .into(moviePoster)

            Glide.with(this)
                .load(baseContext.getString(com.gtools.gmovies.core.R.string.baseUrlImage, movieDetail.posterPath))
                .into(posterHeader)

            isFavorite = movieDetail.favorite
            setFavoriteState(isFavorite)
        }
    }

    private fun addToFavorite(movie: Movie?) {
        if (movie != null) {
            isFavorite = !isFavorite
            viewModel.setFavoriteMovie(movie, isFavorite)
            setFavoriteState(isFavorite)
        }
    }

    private fun setFavoriteState(value: Boolean) {
        if (value) {
            binding.favBtn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.baseline_favorite_filled_24,0,0,0
            )
        } else {
            binding.favBtn.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.baseline_favorite_border_24,0,0,0
            )
        }
    }
}