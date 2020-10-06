package pl.mobite.playground.domain.home.mvi

import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.domain.home.mvi.impl.HomeAction
import pl.mobite.playground.domain.home.mvi.impl.HomeActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResultReducer
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
import kotlinx.coroutines.CoroutineScope
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.processing.MviResultProcessing
import pl.mobite.playground.domain.home.mvi.impl.HomeViewStateCache

/**
 * This is a kind of boilerplate code which is written in order to provide a strong types for all
 * mvi related classes from one feature. It is done in order to simplify dependency injection which
 * in case of Koin relies on class types.
 *
 * An alternative solution is to use Koin named() qualifiers, in example:
 * ```
 * factory(named("HomeActionProcessor")) {
 *     HomeActionProcessor(...)
 * }
 *
 * factory(named("HomeActionProcessingFLow")) {
 *     MviActionProcessingFlow<MviAction, MviResult>(
 *         mviActionProcessor = get(named("HomeActionProcessor"))
 *     )
 * }
 *
 * factory(named("HomeMviController")) { (...) ->
 *     MviController<MviAction, MviResult, MviViewState>(
 *         mviActionProcessingFlow = get(named("HomeActionProcessingFLow")),
 *         ...
 *     )
 * }
 * ```
 * However (in my opinion) providing strong type is less error prone then named() qualifiers.
 */

class HomeMviController(
    homeActionProcessing: HomeActionProcessing,
    homeResultProcessing: HomeResultProcessing,
    coroutineScope: CoroutineScope
) : MviController<HomeAction, HomeResult, HomeViewState>(
    mviActionProcessing = homeActionProcessing,
    mviResultProcessing = homeResultProcessing,
    coroutineScope = coroutineScope
)

class HomeActionProcessing(
    homeActionProcessor: HomeActionProcessor
) : MviActionProcessing<HomeAction, HomeResult>(
    mviActionProcessor = homeActionProcessor
)

class HomeResultProcessing(
    homeResultReducer: HomeResultReducer,
    homeViewStateCache: HomeViewStateCache
) : MviResultProcessing<HomeResult, HomeViewState>(
    mviViewStateCache = homeViewStateCache,
    mviResultReducer = homeResultReducer
)

