package pl.mobite.playground.common.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.mobite.playground.common.CommonFlow
import pl.mobite.playground.common.asCommonFlow
import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.processing.MviResultProcessingProvider

/**
 * Wrapper around whole Mvi flow: MviAction -> processing -> MviResult -> reducing -> MviViewState
 *
 * MviAction's are consumed with `fun accept(...)` and flow with MviViewStates is available with
 * the property `val viewStatesFlow`
 *
 * @param mviActionProcessingProvider
 * @param mviResultProcessingProvider
 * @param mviViewStateCache
 * @param coroutineScope
 */
open class MviController<A : MviAction, R : MviResult, VS : MviViewState>(
    private val mviActionProcessing: MviActionProcessing<A, R>,
    mviResultProcessingProvider: MviResultProcessingProvider<R, VS>,
    private val mviViewStateCache: MviViewStateCache<VS>,
    private val coroutineScope: CoroutineScope,
) {
    private val mviResultProcessing = mviResultProcessingProvider.get(mviViewStateCache.get())

    val viewStatesFlow: CommonFlow<VS> = mviResultProcessing
        .viewStatesFlow
        .asCommonFlow(coroutineScope)

    init {
        mviActionProcessing.resultsFlow
            .onEach(mviResultProcessing::accept)
            .launchIn(coroutineScope)

        mviResultProcessing.savableOutput
            .onEach(mviViewStateCache::set)
            .launchIn(coroutineScope)
    }

    fun accept(intent: VS.() -> A?) {
        coroutineScope.launch {
            mviActionProcessing.accept(
                action = intent(mviResultProcessing.currentViewState()) ?: return@launch
            )
        }
    }

    fun accept(result: R) {
        coroutineScope.launch {
            mviResultProcessing.accept(result)
        }
    }

    /** Used on iOS implementation of the framework */
    fun defaultViewState(): VS = mviResultProcessing.defaultViewState()
}

@Suppress("EXPERIMENTAL_API_USAGE")
open class MviControllerProvider<A : MviAction, R : MviResult, VS : MviViewState>(
    private val mviActionProcessing: MviActionProcessing<A, R>,
    private val mviResultProcessingProvider: MviResultProcessingProvider<R, VS>,
) {
    fun get(
        mviViewStateCache: MviViewStateCache<VS>,
        coroutineScope: CoroutineScope,
    ) = MviController(
        mviActionProcessing = mviActionProcessing,
        mviResultProcessingProvider = mviResultProcessingProvider,
        mviViewStateCache = mviViewStateCache,
        coroutineScope = coroutineScope
    )
}
