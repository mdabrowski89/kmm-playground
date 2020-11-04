package pl.mobite.playground.common.mvi.api

import kotlinx.coroutines.flow.Flow

/**
 * Describes how one [MviAction] object instance is transforming into flow of [MviResult]'s.
 *
 * By convention - each [MviAction] type should have its own [MviActionProcessor].
 */
interface MviActionProcessor<A : MviAction, R : MviResult> {

    operator fun invoke(action: A): Flow<R>
}