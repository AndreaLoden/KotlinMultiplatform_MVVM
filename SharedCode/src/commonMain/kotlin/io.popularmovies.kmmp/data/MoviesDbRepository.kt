package io.popularmovies.kmmp.data

import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.data.source.network.INetworkDataSource
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.domain.usecase.github.getRepos.GetGitHubRepoListRequest
import io.popularmovies.kmmp.domain.MovieContainer


class MoviesDbRepository(
    private val networkDataSource: INetworkDataSource
) {

    /***********************************************************************************************
     * GET REPOS
     **********************************************************************************************/
    suspend fun getPopularMovies(request: GetPopularMoviesListRequest): Response<List<MovieContainer.Movie>> {
        val response = networkDataSource.getPopularMovies()
        return response
    }
}
