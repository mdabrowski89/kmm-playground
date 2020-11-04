package pl.mobite.playground.common.mvi

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.api.MviViewState

/**
 * either this shoud be in androidApp ui.base (where MviEventsCache is)
 * or ui.base MviEventCache & MviEventCacheManager and SaveStateListener
 * should be in this package (imho first one is the valid one)
 * */
abstract class MviViewStateCache<VS : MviViewState>(
    private val savedStateHandle: SavedStateHandle
) {
    private val mviViewStateKey = "mvi.cache.states.${this::class}"

    fun get() = savedStateHandle.get<VS>(mviViewStateKey)

    fun set(viewState: VS) {
        if (isSavable(viewState)) {
            savedStateHandle.set(mviViewStateKey, fold(viewState))
        }
    }

    /**
     * Decide weather this particular instance of MviViewState could be saved in MviViewStateCache.
     * MviViewState should not be saved if it is representing a pending async operation which
     * will be interrupted when values from cache needs to be restored (like a network call etc.)
     */
    protected abstract fun isSavable(viewState: VS): Boolean

    /**
     * Fold MviViewState before it is saved to MviViewStateCache.
     * In this function heavy object references should be cleared because the default cache relies
     * on Android Bundle object and its maximum size is restricted.
     */
    protected open fun fold(viewState: VS): VS = viewState
}