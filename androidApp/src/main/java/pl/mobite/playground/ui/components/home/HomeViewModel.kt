package pl.mobite.playground.ui.components.home

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import pl.mobite.playground.ui.base.MviEventsCache
import pl.mobite.playground.ui.base.MviViewModel

/**
 * View model is used only to provide proper scope to MviController.
 * MviController could be used without ViewModel with some custom scope.
 */
class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    viewStateCache: HomeViewStateCache,
    initialViewState: HomeViewState,
) : MviViewModel() {

    val homeMviController: HomeMviController by mviController(viewStateCache.get() ?: initialViewState)
    val homeMviEventsCache = MviEventsCache(javaClass.name).apply { load(savedStateHandle) }

    init {
        viewStateCache.useWith(homeMviController.viewStatesFlow)
    }

    override fun onCleared() {
        super.onCleared()
        homeMviEventsCache.save(savedStateHandle)
    }
}
