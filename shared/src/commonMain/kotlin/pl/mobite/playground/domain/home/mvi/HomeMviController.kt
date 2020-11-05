package pl.mobite.playground.domain.home.mvi

import pl.mobite.playground.common.mvi.MviController
import kotlinx.coroutines.CoroutineScope
import pl.mobite.playground.domain.home.mvi.impl.*

class HomeMviController(
    actionProcessing: HomeActionProcessing,
    resultReducer: HomeResultReducer,
    initialViewState: HomeViewState,
    coroutineScope: CoroutineScope
) : MviController<HomeAction, HomeResult, HomeViewState>(
    actionProcessing = actionProcessing,
    resultReducer = resultReducer,
    initialViewState = initialViewState,
    coroutineScope = coroutineScope
)

