package com.jarroyo.sharedcode.data.source.network

import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import io.popularmovies.kmmp.domain.MovieContainer

abstract class INetworkDataSource {
    abstract suspend fun getGitHubRepoList(userName: String): Response<List<GitHubRepo>>
    abstract suspend fun getPopularMovies(): Response<List<MovieContainer.Movie>>
}
