package com.religada.moviesdemo.widget

/**
 * Generic Response to manage execution flow through different layers
 */
sealed class OnResult<T> {

    /**
     * Response variant for communicate a Successfully [OnResult]
     * @property data the desired Data this Response is attached to
     */
    data class Success<T>(val data: T) : OnResult<T>()

    /**
     * Response variant for communicate a Failure [OnResult]
     * @property error description of the error
     * @property code errorCode
     */
    data class Error<T>(val error: String = "", val code: Int = -1) : OnResult<T>()
}
