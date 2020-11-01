package pl.mobite.playground.common

import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviViewState

abstract class Store<A: MviAction, VS: MviViewState> {
    abstract val initialState: VS
    abstract fun stateObserver(observer: (VS) -> Unit)
    abstract fun dispatch(intent: (VS) -> A?)
    abstract fun dispose()
}

abstract class MviStore<A: MviAction, R: MviResult, VS: MviViewState>(
    internal val storeScope: CoroutineScope = MainScope()
): KoinComponent, Store<A, VS>() {
    abstract val mviController: MviController<A, R, VS>

    override val initialState: VS
        get() = mviController.defaultViewState()

    override fun stateObserver(observer: (VS) -> Unit) {
        mviController.viewStatesFlow.watch(observer)
    }

    override fun dispatch(intent: (VS) -> A?) {
        mviController.accept(intent)
    }

    override fun dispose() = storeScope.cancel()
}

class PreviewStore<A: MviAction, VS: MviViewState>(
    val state: VS
): Store<A, VS>() {

    override val initialState: VS
        get() = state

    override fun stateObserver(observer: (VS) -> Unit) {
        // no-op
    }

    override fun dispatch(intent: (VS) -> A?) {
        // no-op
    }

    override fun dispose() {
        // no-op
    }
}