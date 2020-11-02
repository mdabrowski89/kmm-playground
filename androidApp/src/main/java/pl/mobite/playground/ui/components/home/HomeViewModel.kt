package pl.mobite.playground.ui.components.home

import pl.mobite.playground.domain.home.HomeViewStateCache
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState

/**
 * View model is used only to provide proper scope to MviController.
 * MviController could be used without ViewModel with some custom scope.
 */
class HomeViewModel(
    cache: HomeViewStateCache,
    initialState: HomeViewState,
) : MviViewModel() {

    val homeMviController: HomeMviController by mviController(cache.get() ?: initialState)

    init {
        cache.useWith(homeMviController.viewStatesFlow)
    }
}
