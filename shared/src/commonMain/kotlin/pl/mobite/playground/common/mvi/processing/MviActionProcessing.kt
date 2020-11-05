package pl.mobite.playground.common.mvi.processing

import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviActionProcessor
import pl.mobite.playground.common.mvi.api.MviResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import pl.mobite.playground.common.mvi.processing.internal.ProcessingFlow

/**
 * Wrapper around [MviActionProcessor].
 *
 * It consumes [MviAction]'s {fun accept(...)} then passes it to [MviActionProcessing] which produces
 * the flow of [MviResult]'s which is available outside with a property `val [resultsFlow]`
 *
 * [process] method is responsible for transforming [MviAction] into flow of [MviResult]
 */
abstract class MviActionProcessing<A : MviAction, R : MviResult> {
    private val input: Channel<A> = Channel(Channel.UNLIMITED)
    private val output: Flow<R> = ProcessingFlow(
        channel = input,
        processing = ::process
    )

    val resultsFlow: Flow<R> = output

    suspend fun accept(action: A) = input.send(action)

    abstract fun process(action: A): Flow<R>
}