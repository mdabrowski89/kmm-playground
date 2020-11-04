package pl.mobite.playground.common.mvi.processing

import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviResultReducer
import pl.mobite.playground.common.mvi.api.MviViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Wrapper around [MviResultReducer].
 *
 * It consumes [MviResult]'s {fun accept(...)} then passes it to [MviResultReducer] which then reduces
 * it with the latest [MviViewState] and produces new [MviViewState] which is available outside with
 * a property `val [viewStatesFlow]`
 *
 * @param initialViewState initial value for an output flow of [MviViewState] - [viewStatesFlow]
 * @param mviResultReducer object responsible for reducing [MviResult] with [MviViewState]
 * and producing new [MviViewState]
 */
@Suppress("EXPERIMENTAL_API_USAGE")
class MviResultProcessing<R : MviResult, VS : MviViewState>(
    initialViewState: VS,
    private val mviResultReducer: MviResultReducer<R, VS>
) {
    private val output = MutableStateFlow(value = initialViewState)

    val viewStatesFlow: Flow<VS> = output

    fun accept(result: R) {
        output.value = with(mviResultReducer) { currentViewState().reduce(result) }
    }

    fun currentViewState(): VS = output.value
}
