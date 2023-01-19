package com.lib.jetutils.NetworkUtils

import com.lib.jetutils.UiText.UiText

/**
 *# tryApiCall
 *
 * This method can be used for network calls when the api handles errors by itself which means the apis should success status codes only on proper success states and return proper error codes and response messages.
 * The results should be handled using [NetworkResult].
 */
suspend fun <T> tryApiCall(block: suspend () -> NetworkResult<T>): NetworkResult<T> =
    try {
        block()
    } catch (e: Exception) {
        NetworkResult.Failure(UiText.DynamicString(e.localizedMessage))
    }

/**
 *# tryApiCallWithCatch
 *
 * This method can be used for network calls when custom error handling is needed .
 * The results should be handled using [NetworkResult].
 */
suspend fun <T> tryApiCallWithCatch(
    tryBlock: suspend () -> NetworkResult<T>,
    catchBlock: suspend (exception: Exception) -> NetworkResult<T>
): NetworkResult<T> =
    try {
        tryBlock()
    } catch (e: Exception) {
        catchBlock(e)
    }