package pl.mobite.playground.common.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.mobite.playground.common.mvi.api.*
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.processing.MviResultProcessing

/**
 * Wrapper around whole Mvi flow:
 * [MviAction] -> [MviActionProcessor] -> [MviResult] -> [MviResultReducer] -> [MviViewState]
 *
 * MviAction's are consumed with `fun accept(...)` and flow with MviViewStates is available with
 * the property `val viewStatesFlow`
 *
 * @param actionProcessing - [MviActionProcessing]
 * @param initialViewState - [MviViewState]
 * @param resultReducer - [MviResult]
 * @param coroutineScope - [CoroutineScope]
 */
open class MviController<A : MviAction, R : MviResult, VS : MviViewState>(
    resultReducer: MviResultReducer<R, VS>,
    initialViewState: VS,
    private val actionProcessing: MviActionProcessing<A, R>,
    private val coroutineScope: CoroutineScope,
) {

    private val mviResultProcessing = MviResultProcessing(initialViewState, resultReducer)

    val viewStatesFlow: Flow<VS> = mviResultProcessing.viewStatesFlow

    init {
        actionProcessing.resultsFlow
            .onEach(mviResultProcessing::accept)
            .launchIn(coroutineScope)
    }

    fun accept(intent: VS.() -> A?) {
        coroutineScope.launch {
            actionProcessing.accept(
                action = intent(mviResultProcessing.currentViewState()) ?: return@launch
            )
        }
    }
}