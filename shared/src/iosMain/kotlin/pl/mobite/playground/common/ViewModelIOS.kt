package pl.mobite.playground.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

open class ViewModelIOS {
    private val viewModelJob = SupervisorJob()

    protected val viewModelScope: CoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main.immediate)

    public fun onCleared() {
        viewModelJob.cancelChildren()
    }
}