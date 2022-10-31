package com.lib.jetutils.NetworkUtils

import androidx.annotation.Keep
import com.lib.jetutils.UiText.UiText


interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}

@Keep
data class PaginationScreenState<T>(
    val isLoading: Boolean = false,
    var items: MutableList<T> = mutableListOf(),
    val error: UiText? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    val isInitialLoad: Boolean = true,
    val isEmpty: Boolean = false,
    val isFilterApplied: Boolean = false
)

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> NetworkResult<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (UiText?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false


    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        result.apply {
            doIfSuccess { items ->
                currentKey = getNextKey(items)
                onSuccess(items, currentKey)
                onLoadUpdated(false)
            }
            doIfFailure {
                onError(it)
                onLoadUpdated(false)

            }
        }

    }

    override fun reset() {
        currentKey = initialKey
    }
}