package pl.mobite.playground.common.mvi.api.test

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.api.*
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.processing.MviResultProcessing

final class TestMviController<A : MviAction, R : MviResult, VS : MviViewState>(
    val viewState: VS
) : MviController<A, R, VS>(
    mviActionProcessing = MviActionProcessing<A, R>(
        mviActionProcessor = object : MviActionProcessor<A, R> {
            override fun invoke(action: A): Flow<R> = emptyFlow()
        }
    ),
    mviResultProcessing = MviResultProcessing<R, VS>(
        mviViewStateCache = object : MviViewStateCache<VS> {
            override fun get(): VS? = null
            override fun set(viewState: VS) {}
        },
        mviResultReducer = object : MviResultReducer<R, VS> {
            override fun default(): VS = viewState
            override fun VS.reduce(result: R): VS = viewState
        }
    ),
    coroutineScope = MainScope()
)
