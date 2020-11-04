package pl.mobite.playground.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.common.mvi.api.MviViewState

open class MviViewModel : ViewModel(), KoinComponent {

    /**
     * Provides all the necessary parameters to inject [MviController]
     */
    inline fun <reified C : MviController<*, *, VS>, VS : MviViewState> mviController(initialState: VS) =
        inject<C> { parametersOf(initialState, viewModelScope) }

    /**
     * Launches parallel [flow] collector which delegates elements to [MviViewStateCache.set]
     */
    protected fun <VS : MviViewState> MviViewStateCache<VS>.useWith(flow: Flow<VS>) {
        flow.onEach(::set).launchIn(viewModelScope)
    }
}