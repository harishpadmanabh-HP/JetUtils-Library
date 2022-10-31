package com.lib.jetutils.NetworkUtils

import com.lib.jetutils.UiText.UiText


sealed class NetworkResult<out T> {
    data class Success<out R>(val value: R) : NetworkResult<R>()
    object Loading : NetworkResult<Nothing>()
    data class Failure(
        val uiText: UiText?
    ) : NetworkResult<Nothing>()
}

inline fun <reified T> NetworkResult<T>.doIfFailure(callback: (error: UiText?) -> Unit) {
    if (this is NetworkResult.Failure) {
        callback(uiText)
    }
}

inline fun <reified T> NetworkResult<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is NetworkResult.Success) {
        callback(value)
    }
}

inline fun <reified T> NetworkResult<T>.doIfLoading(callback: () -> Unit) {
    if (this is NetworkResult.Loading) {
        callback()
    }
}