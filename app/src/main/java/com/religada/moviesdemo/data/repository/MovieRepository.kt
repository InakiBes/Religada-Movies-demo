package com.religada.moviesdemo.data.repository

import com.religada.moviesdemo.BuildConfig
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.data.model.PopularMoviesResponse
import com.religada.moviesdemo.data.remote.ApiConnection
import com.religada.moviesdemo.utils.log
import com.religada.moviesdemo.widget.OnResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(){

    fun getPopularMovies(page: Int, language:String, onResponse: (OnResult<List<MovieResponse>>) -> Unit) {
        val apiKey = BuildConfig.API_KEY
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retroService = ApiConnection.RETRO_SERVICE_NO_AUTH
                val apiResponse: Response<PopularMoviesResponse> = retroService.getPopularMovies(page, language, apiKey)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { response ->
                        withContext(Dispatchers.Main) {
                            onResponse(OnResult.Success(response.movies))
                        }
                    }
                } else {
                    apiResponse.errorBody()?.let { responseBody ->
                        try {
                            val errorResponse = JSONObject(responseBody.string())
                            val errorCode : Int = errorResponse["status_code"] as Int
                            val errorMsg: String = errorResponse["status_message"] as String
                            log("Error api $errorMsg - Code: $errorCode")
                            onResponse(OnResult.Error(errorMsg, errorCode))
                        } catch (e: Exception) {
                            onResponse(OnResult.Error("Unknow error ih API response", -1))
                            log("Error ${e.message}")
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                onResponse(OnResult.Error("Error calling API", -1))
            }
        }
    }
}