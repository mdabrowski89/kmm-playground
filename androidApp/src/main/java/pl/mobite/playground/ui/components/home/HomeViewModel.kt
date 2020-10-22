package pl.mobite.playground.ui.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import pl.mobite.playground.domain.home.mvi.HomeControllerProvider
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState

/**
 * View model is used only to provide proper scope to MviController.
 * MviController could be used without ViewModel with some custom scope.
 */
class HomeViewModel(
    cache: MviViewStateCache<HomeViewState>,
    homeControllerProvider: HomeControllerProvider
) : ViewModel() {

    val homeMviController = homeControllerProvider.get(
        mviViewStateCache = cache,
        coroutineScope = viewModelScope
    )
}
