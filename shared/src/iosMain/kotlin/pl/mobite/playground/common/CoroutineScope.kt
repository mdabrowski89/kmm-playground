package pl.mobite.playground.common

import kotlinx.coroutines.*

class CoroutineScopeIOS(val scope: CoroutineScope) {
    fun cancel() = scope.cancel()
}
