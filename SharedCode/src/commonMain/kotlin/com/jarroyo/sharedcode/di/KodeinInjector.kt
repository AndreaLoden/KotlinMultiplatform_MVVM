package com.jarroyo.sharedcode.di

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.ApplicationDispatcher
import com.jarroyo.sharedcode.data.repository.CounterRepository
import com.jarroyo.sharedcode.data.repository.GitHubRepository
import com.jarroyo.sharedcode.data.source.network.INetworkDataSource
import com.jarroyo.sharedcode.data.source.network.NetworkDataSource
import com.jarroyo.sharedcode.domain.usecase.counter.GetCounterUseCase
import com.jarroyo.sharedcode.domain.usecase.github.getRepos.GetGitHubRepoListUseCase
import io.popularmovies.kmmp.data.MoviesDbApi
import io.popularmovies.kmmp.data.PopularMovieListRepository
import io.popularmovies.kmmp.domain.GetPopularMoviesListUseCase
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
val KodeinInjector = Kodein {

    bind<CoroutineContext>() with provider { ApplicationDispatcher }

    /**
     * USECASES
     */
    bind<GetCounterUseCase>() with singleton { GetCounterUseCase(instance()) }
    bind<GetGitHubRepoListUseCase>() with singleton { GetGitHubRepoListUseCase(instance()) }
    bind<GetPopularMoviesListUseCase>() with singleton { GetPopularMoviesListUseCase(instance()) }

    /**
     * REPOSITORIES
     */
    bind<CounterRepository>() with provider { CounterRepository() }
    bind<GitHubRepository>() with provider { GitHubRepository(instance()) }
    bind<PopularMovieListRepository>() with provider { PopularMovieListRepository(instance()) }

    /**
     * NETWORK DATA SOURCE
     */
    bind<INetworkDataSource>() with provider { NetworkDataSource(instance(), instance()) }

    /**
     * NETWORK API
     */
    bind<GitHubApi>() with provider { GitHubApi() }
    bind<MoviesDbApi>() with provider { MoviesDbApi() }
}
