package pl.mobite.playground.domain.home.mvi.impl

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import pl.mobite.playground.common.mvi.impl.MviViewStateCacheAndroid

actual class HomeViewStateCache(
    savedStateHandle: SavedStateHandle
) : MviViewStateCache<HomeViewState> by MviViewStateCacheAndroid(
    key = "HomeViewStateKey",
    savedStateHandle = savedStateHandle
)