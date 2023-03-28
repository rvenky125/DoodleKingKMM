package com.famas.doodlekingkmm.domain

sealed class Response<T> {
    data class Loading<T>(val isLoading: Boolean): Response<T>()
    data class Success<T>(val data: T?, val message: String? = null): Response<T>()
    data class Failure<T>(val message: String, val data: T? = null): Response<T>()
}