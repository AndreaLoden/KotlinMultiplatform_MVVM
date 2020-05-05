package io.popularmovies.kmmp.presentation.movielist

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.jarroyo.kmp_mvvm.R
import com.jarroyo.sharedcode.base.Response
import io.popularmovies.kmmp.HostActivity
import io.popularmovies.kmmp.domain.MovieContainer
import io.popularmovies.kmmp.domain.map
import io.popularmovies.kmmp.model.Movie
import io.popularmovies.kmmp.presentation.*
import io.popularmovies.kmmp.presentation.movielist.recyclerview.MovieListAdapter
import io.popularmovies.kmmp.presentation.movielist.recyclerview.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_movie_list.*
import java.util.*

/**
 * A fragment that shows popular movie posters on a recycler view
 */
class MovieListFragment : Fragment(), MovieListAdapter.MovieClickListener{

    private var gridLayoutManager: GridLayoutManager? = null

    private val moviesAdapter by lazy { MovieListAdapter(this) }

    lateinit var movieListViewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }

    fun getPopularMoviesstate(state: GetPopularMoviesState) {
        when (state) {

            is SuccessGetPopularMoviesState -> {
                progress_bar.visibility = View.GONE
                val response = state.response as Response.Success
                showMovieList(response.data)
            }

            is LoadingGetPopularMoviesState -> {
                progress_bar.visibility = View.VISIBLE
            }

            is ErrorGetPopularMoviesState -> {
                progress_bar.visibility = View.GONE
                val response = state.response as Response.Error
                errorView.text = response.message
                errorView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    private fun showMovieList(movieList: List<MovieContainer.Movie>) {
        moviesAdapter.setData(movieList)
        moviesAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        setupRecyclerView()

        movieListViewModel.mGetPopularMovieListLiveData.addObserver { getPopularMoviesstate(it) }

        if (savedInstanceState == null && moviesAdapter.getData().isEmpty()) {
            movieListViewModel.getPopularMoviesList()
        }

        Handler().postDelayed({ startPostponedEnterTransition() }, 300)
    }

    override fun onResume() {
        super.onResume()
        (activity as HostActivity).setToolbarTitle(getString(R.string.app_name))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, gridLayoutManager?.onSaveInstanceState())
        outState.putSerializable(BUNDLE_MOVIES, moviesAdapter.getData().map {
            with(it) {
                Movie(
                    id,
                    title,
                    overview,
                    vote_average,
                    release_date,
                    poster_path
                )
            }
        }.toTypedArray())
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        try {
            (savedInstanceState?.getSerializable(BUNDLE_MOVIES) as ArrayList<Movie>?)?.let {
                showMovieList(it.map {
                    with(it) {
                        MovieContainer.Movie(
                            id,
                            originalTitle,
                            plotSynopsis,
                            userRating,
                            releaseDate,
                            imageThumbnailUrl
                        )
                    }
                })
            }

            val state = savedInstanceState?.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT)
            gridLayoutManager?.onRestoreInstanceState(state)

        } catch (cce: ClassCastException) {
            Log.d("MovieListFragment", "Unable to deserialize passed movies")
        }

        super.onActivityCreated(savedInstanceState)
    }

    /**********************************************************************************************
     * Implementation of [MovieListAdapter.MovieClickListener]
     *********************************************************************************************/
    override fun onMovieClicked(movie: MovieContainer.Movie, transitionView: View) {
        (activity as MovieNavigator).navigateToMovieDetailFragment(movie, this, transitionView)
    }

    /**********************************************************************************************
     * Private methods
     *********************************************************************************************/

    private fun setupRecyclerView() {
        val spanCount = getSpanCount()
        gridLayoutManager = GridLayoutManager(context, spanCount)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.movie_item_margin)

        recycler_view_movies.addItemDecoration(SpacesItemDecoration(spanCount, spacingInPixels))
        recycler_view_movies.layoutManager = gridLayoutManager
        recycler_view_movies.setHasFixedSize(true)
        recycler_view_movies.adapter = moviesAdapter
    }

    private fun getSpanCount(): Int = resources.getInteger(R.integer.span_count)

    companion object {
        private const val BUNDLE_RECYCLER_LAYOUT = "mainactivity.recycler.layout"
        private const val BUNDLE_MOVIES = "mainactivity.data.movies"
    }
}
