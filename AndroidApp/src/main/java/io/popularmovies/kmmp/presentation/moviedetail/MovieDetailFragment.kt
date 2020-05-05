package io.popularmovies.kmmp.presentation.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarroyo.kmp_mvvm.R
import com.squareup.picasso.Picasso
import io.popularmovies.kmmp.*
import io.popularmovies.kmmp.domain.MovieContainer
import io.popularmovies.kmmp.domain.MovieDetailCase
import io.popularmovies.kmmp.domain.map
import io.popularmovies.kmmp.model.Movie
import io.popularmovies.kmmp.presentation.MovieDetailPresenter
import io.popularmovies.kmmp.presentation.MovieDetailState
import io.popularmovies.kmmp.presentation.MovieDetailView
import io.popularmovies.kmmp.presentation.moviedetail.recyclerview.ReviewsAdapter
import io.popularmovies.kmmp.presentation.moviedetail.recyclerview.TrailersAdapter
import kotlinx.android.synthetic.main.detail_fragment_content.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MovieDetailFragment : Fragment(),
    MovieDetailView, KodeinAware {

    override val kodein by kodein()

    private val detailCase: MovieDetailCase by instance<MovieDetailCase>()

    private var trailersAdapter: TrailersAdapter? = null
    private var reviewsAdapter: ReviewsAdapter? = null

    /**********************************************************************************************
     * Lifecycle callbacks
     *********************************************************************************************/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.movie_detail_fragment, container, false)

        postponeEnterTransition()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.containsKey(ARG_MOVIE) == true) {
            (arguments?.getSerializable(ARG_MOVIE) as Movie).let { itMovie ->

                MovieDetailPresenter(
                    this,
                    detailCase
                ).start(itMovie.id ?: "")

                itMovie.originalTitle?.let { (activity as HostActivity).setToolbarTitle(it) }

                tv_plot_content.text = itMovie.plotSynopsis
                tv_release_date_content.text = itMovie.releaseDate
                tv_rating_content.text =
                    getString(R.string.detail_rating_out_of, itMovie.userRating)

                ViewCompat.setTransitionName(view.findViewById<View>(R.id.image_iv), itMovie.id)
                Picasso.get().load(itMovie.getFormattedImageThumbnailUrl()).into(image_iv)

                // Data is loaded so lets wait for our parent to be drawn
                (view.parent as? ViewGroup)?.doOnPreDraw {
                    // Parent has been drawn. Start transitioning!
                    startPostponedEnterTransition()
                }
            }
        }

        context?.let {
            reviewsAdapter = ReviewsAdapter(it)
            tv_reviews_list.adapter = reviewsAdapter
            tv_reviews_list.layoutManager = LinearLayoutManager(it)

            trailersAdapter = TrailersAdapter(it)
            tv_trailers_list.adapter = trailersAdapter
            tv_trailers_list.layoutManager = LinearLayoutManager(it)
        }
    }

    companion object {
        private const val ARG_MOVIE = "movie"

        fun newInstance(movie: MovieContainer.Movie): MovieDetailFragment {

            val args = Bundle()
            args.putSerializable(ARG_MOVIE, with(movie) {
                Movie(
                    id,
                    title,
                    overview,
                    vote_average,
                    release_date,
                    poster_path
                )
            })

            val fragment = MovieDetailFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun showState(state: MovieDetailState) {

        state.movieReviewsResponse.map {
            tv_reviews_label.visibility = View.VISIBLE
            reviewsAdapter?.setData(it)
            reviewsAdapter?.notifyDataSetChanged()
        }

        state.movieTrailers.map {
            tv_trailers_label.visibility = View.VISIBLE

            trailersAdapter?.setData(it)
            trailersAdapter?.notifyDataSetChanged()
        }
    }
}
