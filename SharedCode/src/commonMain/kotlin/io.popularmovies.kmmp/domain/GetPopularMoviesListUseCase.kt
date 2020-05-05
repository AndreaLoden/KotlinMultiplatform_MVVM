package io.popularmovies.kmmp.domain

import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.data.repository.GitHubRepository
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.domain.usecase.base.BaseUseCase
import io.popularmovies.kmmp.data.GetPopularMoviesListRequest
import io.popularmovies.kmmp.data.MoviesDbRepository


open class GetPopularMoviesListUseCase(val repository: MoviesDbRepository) : BaseUseCase<GetPopularMoviesListRequest, List<MovieContainer.Movie>>() {

    override suspend fun run(): Response<List<MovieContainer.Movie>> {
        return repository.getPopularMovies(request!!)
    }
}
