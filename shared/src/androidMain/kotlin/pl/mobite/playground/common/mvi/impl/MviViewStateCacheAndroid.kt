package pl.mobite.playground.common.mvi.impl

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.api.MviViewStateCache

open class MviViewStateCacheAndroid<VS : MviViewState>(
    private val key: String,
    private val savedStateHandle: SavedStateHandle
) : MviViewStateCache<VS> {

    override fun get() = savedStateHandle.get<VS>(key)

    override fun set(viewState: VS) = savedStateHandle.set(key, viewState)
}