package pl.mobite.playground.ui.base.viewbinding

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * This observer removes itself from [LifecycleOwner] observers when
 * [androidx.lifecycle.Lifecycle.State.DESTROYED] is reached
 * */
open class SelfRemoveLifecycleObserver : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}

/**
 * Triggers [action] when [LifecycleOwner] reaches
 * [androidx.lifecycle.Lifecycle.State.DESTROYED] state.
 * */
class OnDestroyLifecycleObserver(
    private val action: (owner: LifecycleOwner) -> Unit
) : SelfRemoveLifecycleObserver() {

    override fun onDestroy(owner: LifecycleOwner) =
        super.onDestroy(owner).also { action(owner) }
}