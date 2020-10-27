package pl.mobite.playground.common.mvi.processing

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.processing.internal.ProcessingFlow

/**
 * It consumes MviAction's {fun accept(...)} then passes it to MviActionProcessing which produces
 * the flow of MviResult's which is available outside with a property `val resultsFlow`
 *
 * @param processing object responsible for transforming MviAction into flow of MviResults
 *
 * TODO: update docs
 */
abstract class MviActionProcessing<A : MviAction, R : MviResult> {
    private val input: Channel<A> = Channel(Channel.UNLIMITED)
    private val output: Flow<R> = ProcessingFlow(
        channel = input,
        processing = ::processing,
    )

    val resultsFlow: Flow<R> = output.transform {
        /*
        * This skips emission of result if post processing consumes it (returns null)
        * */
        emit(postProcessing(it) ?: return@transform)
    }

    suspend fun accept(action: A) = input.send(action)

    abstract fun processing(action: A): Flow<R>
    open suspend fun postProcessing(result: R): R? = result
}
