package pl.mobite.playground.common.mvi.processing

import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviResultReducer
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import pl.mobite.playground.common.mvi.api.MviViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

/**
 * Provider enables late initialization for [MviResultReducer] otherwise dependency would
 * had to be provided during dependency injection state. This relaxes configuration stage
 *
 * @param mviResultReducer reducer which will be seeded with provided initialValue
 * within [get] method
 * */
@Suppress("EXPERIMENTAL_API_USAGE")
open class MviResultProcessingProvider<R : MviResult, VS : MviViewState>(
    private val mviResultReducer: MviResultReducer<R, VS>
) {
    fun get(initialValue: VS?) = MviResultProcessing(
        initialValue = initialValue,
        mviResultReducer = mviResultReducer,
    )
}

/**
 * Wrapper around MviResultReducer.
 *
 * It consumes MviResult's {fun accept(...)} then passes it to MviResultReducer which then reduces
 * it with the latest MviViewState and produces new MviViewState which is available outside with
 * a property `val viewStatesFlow`
 *
 * @param initialValue if value is provided (not null) then it will be used to seed initial state
 * otherwise [MviResultReducer.default] will be used
 * @param mviResultReducer object responsible for reducing MviResults with MviViewState
 * and producing new MviViewState
 */
@Suppress("EXPERIMENTAL_API_USAGE")
open class MviResultProcessing<R : MviResult, VS : MviViewState>(
    initialValue: VS?,
    private val mviResultReducer: MviResultReducer<R, VS>
) {
    private val output = MutableStateFlow(value = initialValue ?: mviResultReducer.default())

    val savableOutput = output
        .filter { it.isSavable() }
        .mapNotNull { mviResultReducer.fold(it) }

    val viewStatesFlow: Flow<VS> = output

    fun accept(result: R) {
        output.value = with(mviResultReducer) { currentViewState().reduce(result) }
    }

    fun currentViewState(): VS = output.value

    /** Used on iOS implementation of the framework */
    fun defaultViewState(): VS = mviResultReducer.default()
}
