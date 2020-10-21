package pl.mobite.playground.domain.home.mvi

import kotlinx.coroutines.CoroutineScope
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.processing.MviActionProcessingProvider
import pl.mobite.playground.common.mvi.processing.MviResultProcessingProvider
import pl.mobite.playground.domain.home.mvi.impl.*

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
    homeActionProcessing: HomeActionProcessingProvider,
    homeResultProcessing: HomeResultProcessingProvider,
    cache: HomeViewStateCache,
    coroutineScope: CoroutineScope
) : MviController<HomeAction, HomeResult, HomeViewState>(
    mviActionProcessingProvider = homeActionProcessing,
    mviResultProcessingProvider = homeResultProcessing,
    mviViewStateCache = cache,
    coroutineScope = coroutineScope
)

class HomeActionProcessingProvider(
    homeActionProcessor: HomeActionProcessor
) : MviActionProcessingProvider<HomeAction, HomeResult>(
    mviActionProcessor = homeActionProcessor
)

class HomeResultProcessingProvider(
    homeResultReducer: HomeResultReducer,
) : MviResultProcessingProvider<HomeResult, HomeViewState>(
    mviResultReducer = homeResultReducer
)

