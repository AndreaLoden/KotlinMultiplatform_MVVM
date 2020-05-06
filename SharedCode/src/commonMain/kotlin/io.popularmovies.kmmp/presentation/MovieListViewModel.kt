package io.popularmovies.kmmp.presentation

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.data.source.network.NetworkDataSource
import com.jarroyo.sharedcode.di.KodeinInjector
import com.jarroyo.sharedcode.utils.coroutines.launchSilent
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.popularmovies.kmmp.data.GetPopularMoviesListRequest
import io.popularmovies.kmmp.data.MoviesDbApi
import io.popularmovies.kmmp.data.MoviesDbRepository
import io.popularmovies.kmmp.domain.GetPopularMoviesListUseCase
import io.popularmovies.kmmp.domain.MovieContainer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class MovieListViewModel : ViewModel() {

    // LIVE DATA
    var mGetPopularMovieListLiveData =
        MutableLiveData<GetPopularMoviesState>(LoadingGetPopularMoviesState())

    // USE CASE
    private val mGetPopularMoviesUseCase = GetPopularMoviesListUseCase(
        MoviesDbRepository(
            NetworkDataSource(
                GitHubApi(),
                MoviesDbApi()
            )
        )
    )

    // ASYNC - COROUTINES
    private val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    /**
     * GET GITHUB REPO LIST
     */
    fun getPopularMoviesList() = launchSilent(coroutineContext, exceptionHandler, job) {
        mGetPopularMovieListLiveData.postValue(LoadingGetPopularMoviesState())

        val request = GetPopularMoviesListRequest()
        val response = mGetPopularMoviesUseCase.execute(request)
        processPopularMoviesListResponse(response)
    }

    /**
     * PROCCESS RESPONSE
     */
    fun processPopularMoviesListResponse(response: Response<List<MovieContainer.Movie>>) {
        if (response is Response.Success) {
            mGetPopularMovieListLiveData.postValue(
                SuccessGetPopularMoviesState(
                    response
                )
            )
        } else if (response is Response.Error) {
            mGetPopularMovieListLiveData.postValue(
                ErrorGetPopularMoviesState(
                    response
                )
            )
        }
    }
}
