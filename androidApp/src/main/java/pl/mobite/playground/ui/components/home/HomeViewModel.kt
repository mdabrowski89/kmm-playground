package pl.mobite.playground.ui.components.home

import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import pl.mobite.playground.ui.base.MviViewModel

/**
 * View model is used only to provide proper scope to MviController.
 * MviController could be used without ViewModel with some custom scope.
 */
class HomeViewModel(
    viewStateCache: HomeViewStateCache,
    initialViewState: HomeViewState,
) : MviViewModel() {

    val homeMviController: HomeMviController by mviController(viewStateCache.get() ?: initialViewState)

    init {
        viewStateCache.useWith(homeMviController.viewStatesFlow)
    }
}
