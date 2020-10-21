package pl.mobite.playground.common.mvi.processing

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import pl.mobite.playground.common.mvi.api.*
import pl.mobite.playground.common.mvi.processing.internal.ProcessingFlow


@Suppress("EXPERIMENTAL_API_USAGE")
open class MviActionProcessingProvider<A : MviAction, R : MviResult>(
    private val mviActionProcessor: MviActionProcessor<A, R>
) {
    fun get() = MviActionProcessing(
        mviActionProcessor = mviActionProcessor
    )
}

/**
 * Wrapper around MviActionProcessors.
 *
 * It consumes MviAction's {fun accept(...)} then passes it to MviActionProcessing which produces
 * the flow of MviResult's which is available outside with a property `val resultsFlow`
 *
 * @param mviActionProcessor object responsible for transforming MviAction into flow of MviResults
 */
open class MviActionProcessing<A : MviAction, R : MviResult>(
    mviActionProcessor: MviActionProcessor<A, R>
) {
    private val input: Channel<A> = Channel(Channel.UNLIMITED)
    private val output: Flow<R> = ProcessingFlow(
        channel = input,
        processor = mviActionProcessor
    )

    val resultsFlow: Flow<R> = output

    suspend fun accept(action: A) = input.send(action)
}