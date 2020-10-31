package pl.mobite.playground.domain.home

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.MviViewStateCache
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState

class HomeViewStateCache(
    savedStateHandle: SavedStateHandle
) : MviViewStateCache<HomeViewState>(
    savedStateHandle = savedStateHandle
) {

    override fun isSavable(viewState: HomeViewState) = !viewState.inProgress
}