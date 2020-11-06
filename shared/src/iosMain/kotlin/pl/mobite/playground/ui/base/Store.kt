package pl.mobite.playground.ui.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviViewState

abstract class Store<A : Any, VS : Any> {
    abstract val initialState: VS
    abstract fun stateObserver(observer: (VS) -> Unit)
    abstract fun dispatch(intent: (VS) -> A?)
    abstract fun dispose()

    var prefix: String? = null
        private set

    fun debug(prefix: String): Store<A, VS> {
        this.prefix = prefix
        return this
    }

    fun debug(): Store<A, VS> {
        return debug(prefix = "")
    }
}

abstract class MviStore<A : MviAction, R : MviResult, VS : MviViewState>(
    internal val storeScope: CoroutineScope = MainScope()
) : KoinComponent, Store<A, VS>() {
    abstract val mviController: MviController<A, R, VS>

    override fun stateObserver(observer: (VS) -> Unit) {
        mviController.viewStatesFlow.onEach { observer(it) }.launchIn(storeScope)
    }

    override fun dispatch(intent: (VS) -> A?) {
        mviController.accept(intent)
    }

    override fun dispose() = storeScope.cancel()
}

class EmptyStore<A : MviAction, VS : MviViewState>(
    override val initialState: VS
) : Store<A, VS>() {

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