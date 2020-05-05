package io.popularmovies.kmmp.data


import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.base.exception.NetworkConnectionException
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.utils.networkSystem.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.popularmovies.kmmp.domain.MovieContainer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

class MoviesDbApi {


    private val baseEndpoint = "https://api.themoviedb.org/3/movie"
    private val apiKeyQueryParam = "api_key=220e2ce24c38e16c4eafe5708e0e39d4"

    private val httpClient = HttpClient()

    suspend fun getPopularMoviesList(): Response<List<MovieContainer.Movie>> {
        try {
            if (isNetworkAvailable()) {
                val url = "$baseEndpoint/popular?$apiKeyQueryParam"
                val json = httpClient.get<String>(url)

                val response = Json.nonstrict.parse(MovieContainer.serializer(), json)
                return Response.Success(response.results)
            } else {
                return Response.Error(NetworkConnectionException())
            }
        } catch (ex: Exception) {
            return Response.Error(ex)
        }
    }
}
