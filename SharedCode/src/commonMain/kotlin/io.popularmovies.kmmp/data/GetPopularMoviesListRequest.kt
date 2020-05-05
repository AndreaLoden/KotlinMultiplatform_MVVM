package io.popularmovies.kmmp.data

import com.jarroyo.sharedcode.domain.usecase.base.BaseRequest

class GetPopularMoviesListRequest() : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
