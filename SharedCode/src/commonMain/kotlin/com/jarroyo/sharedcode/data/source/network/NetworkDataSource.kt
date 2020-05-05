package com.jarroyo.sharedcode.data.source.network

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import io.popularmovies.kmmp.data.MoviesDbApi
import io.popularmovies.kmmp.domain.MovieContainer

class NetworkDataSource(
    private val gitHubApi: GitHubApi,
    private val moviesDbApi: MoviesDbApi
) : INetworkDataSource() {
    /**
     * GET GITHUB REPO LIST
     */
    override suspend fun getGitHubRepoList(userName: String): Response<List<GitHubRepo>> {
        return gitHubApi.getGitHubRepoList(userName)
    }

    override suspend fun getPopularMovies(): Response<List<MovieContainer.Movie>> {
        return moviesDbApi.getPopularMoviesList()
    }
}
