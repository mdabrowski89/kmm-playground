package pl.mobite.playground.common.mvi

import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.processing.MviResultProcessing
import pl.mobite.playground.common.mvi.api.MviViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Wrapper around whole Mvi flow: MviAction -> processing -> MviResult -> reducing -> MviViewState
 *
 * MviAction's are consumed with `fun accept(...)` and flow with MviViewStates is available with
 * the property `val viewStatesFlow`
 *
 * @param mviActionProcessing
 * @param mviResultProcessing
 * @param coroutineScope
 */
open class MviController<A : MviAction, R : MviResult, VS : MviViewState>(
    private val mviActionProcessing: MviActionProcessing<A, R>,
    private val mviResultProcessing: MviResultProcessing<R, VS>,
    private val coroutineScope: CoroutineScope
) {
    val viewStatesFlow: Flow<VS> = mviResultProcessing.viewStatesFlow

    init {
        mviActionProcessing.resultsFlow
            .onEach(mviResultProcessing::accept)
            .launchIn(coroutineScope)

        mviResultProcessing.savableOutput.launchIn(coroutineScope)
    }

    fun accept(intent: VS.() -> A?) {
        coroutineScope.launch {
            mviActionProcessing.accept(
                action = intent(mviResultProcessing.currentViewState()) ?: return@launch
            )
        }
    }
}