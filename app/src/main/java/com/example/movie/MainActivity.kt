package com.example.movie

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.movie.data.remote.MovieService
import com.example.movie.database.MovieDatabase
import com.example.movie.database.model.MovieEntity
import com.example.movie.ui.MovieAdapter
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.MovieViewModelFactory
import com.example.movie.viewmodel.events.FilterType
import com.example.movie.viewmodel.events.MovieEvents
import com.example.movie.viewmodel.repositories.GenreRepository
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movie.R
import com.example.movie.database.GenreDatabase
import com.example.movie.database.model.GenreEntity
import com.example.movie.ui.DetailDialogFragment
import com.example.movie.ui.GenreColors

class MainActivity : AppCompatActivity() {
    private val service = MovieService.create()
    private lateinit var recycledView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: MovieAdapter
    private var movies: List<MovieEntity> = emptyList()
    private var genreList: List<GenreEntity> = emptyList()
    private lateinit var tabLayout: TabLayout

    private lateinit var db: MovieDatabase
    private lateinit var genreDB: GenreDatabase
    private lateinit var genreRepository: GenreRepository
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room
            .databaseBuilder(
                applicationContext,
                MovieDatabase::class.java,
                "movie_db")
            .fallbackToDestructiveMigration()
            .build()


        genreDB = Room
            .databaseBuilder(
                applicationContext,
                GenreDatabase::class.java,
                "genre_db")
            .build()

        GenreColors.initialize(this)
        genreRepository = GenreRepository(genreDB.dao)

        viewModel = ViewModelProvider(this, MovieViewModelFactory(application, db.dao, genreRepository)).get(MovieViewModel::class.java)

        tabLayout = findViewById(R.id.tabLayout)

        manager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        myAdapter = MovieAdapter(movies) { movie ->
            viewModel.onEvent(MovieEvents.ShowMovieInfo(movie))
        }

        recycledView = findViewById<RecyclerView>(R.id.rvMovies).apply {
            layoutManager = manager
            adapter = myAdapter
        }

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.popularity)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.rating)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.favorites)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.newest)))

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.customView = layoutInflater.inflate(R.layout.custom_tab, null)
            tab?.customView?.findViewById<TextView>(R.id.tab_text)?.text = tab?.text
        }

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.onEvent(MovieEvents.FilterMovies(FilterType.POPULARITY))
                    1 -> viewModel.onEvent(MovieEvents.FilterMovies(FilterType.RATING))
                    2 -> viewModel.onEvent(MovieEvents.FilterMovies(FilterType.FAVORITES))
                    3 -> viewModel.onEvent(MovieEvents.FilterMovies(FilterType.RELEASE_DATE))
                }
                tab.customView?.findViewById<TextView>(R.id.tab_text)?.let {
                    applyGradientToText(it)
                }
                recycledView.stopScroll()
                recycledView.smoothScrollToPosition(0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tab_text)?.let {
                    it.paint.shader = null // Remove gradient
                    it.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                recycledView.stopScroll()
                recycledView.smoothScrollToPosition(0)
            }
        })

        val initialTab = tabLayout.getTabAt(tabLayout.selectedTabPosition)
        initialTab?.customView?.findViewById<TextView>(R.id.tab_text)?.let {
            applyGradientToText(it)
        }

        lifecycleScope.launch {
            try {
                val genreResponse = service.getGenres()
                genreList = genreResponse.genres
                viewModel.insertAllGenres(genreList)
                viewModel.getAllGenres()
                val movieResponse = service.getMovies()
                movies = movieResponse.results
                movies.forEach { movie ->
                    db.dao.insertMovie(movie)
                }
                viewModel.state.collect { state ->
                    println("[LOGXD] ${state.movies}")
                    myAdapter.submitList(state.movies)

                    if (state.isReadingInfo) {
                        showDetailDialog(state.movieInfo)
                        viewModel.onEvent(MovieEvents.HideMovieInfo)
                    }
                }
            } catch (e: Exception) {
                // Handle possible errors here
                println("bruh" + e.message.toString())
            }
        }
    }

    private fun showDetailDialog(movie: MovieEntity?) {
        if (movie == null) {
            println("No movie yet")
        } else {
            val dialogFragment = DetailDialogFragment.newInstance(movie)
            dialogFragment.show(supportFragmentManager, "detailDialog")
        }
    }

    private fun applyGradientToText(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                ContextCompat.getColor(this, R.color.gradient_start),
                ContextCompat.getColor(this, R.color.gradient_end)
            ),
            null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }
}
