package io.popularmovies.kmmp.presentation

import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import io.popularmovies.kmmp.domain.MovieContainer


sealed class GetPopularMoviesState {
    abstract val response: Response<List<MovieContainer.Movie>>?
}
data class SuccessGetPopularMoviesState(override val response: Response<List<MovieContainer.Movie>>) : GetPopularMoviesState()
data class LoadingGetPopularMoviesState(override val response: Response<List<MovieContainer.Movie>>? = null) : GetPopularMoviesState()
data class ErrorGetPopularMoviesState(override val response: Response<List<MovieContainer.Movie>>) : GetPopularMoviesState()
