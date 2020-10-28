package pl.mobite.playground.common

import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.api.MviAction
import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.common.mvi.api.MviViewState

abstract class ViewModel<A: MviAction, R: MviResult, VS: MviViewState>(
    internal val viewModelScope: CoroutineScope
): KoinComponent {
    abstract val mviController: MviController<A, R, VS>
    fun close() = viewModelScope.cancel()
}
