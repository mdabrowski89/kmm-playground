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
    initialViewState: HomeViewState,
    resultReducer: HomeResultReducer,
    coroutineScope: CoroutineScope
) : MviController<HomeAction, HomeResult, HomeViewState>(
    actionProcessor = actionProcessor,
    initialViewState = initialViewState,
    resultReducer = resultReducer,
    coroutineScope = coroutineScope
)

