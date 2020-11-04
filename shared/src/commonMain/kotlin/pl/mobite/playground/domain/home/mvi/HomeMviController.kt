package pl.mobite.playground.domain.home.mvi

import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.domain.home.mvi.impl.HomeAction
import pl.mobite.playground.domain.home.mvi.impl.HomeActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResultReducer
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import kotlinx.coroutines.CoroutineScope

class HomeMviController(
    actionProcessor: HomeActionProcessor,
    resultReducer: HomeResultReducer,
    initialViewState: HomeViewState,
    coroutineScope: CoroutineScope
) : MviController<HomeAction, HomeResult, HomeViewState>(
    actionProcessor = actionProcessor,
    resultReducer = resultReducer,
    initialViewState = initialViewState,
    coroutineScope = coroutineScope
)

