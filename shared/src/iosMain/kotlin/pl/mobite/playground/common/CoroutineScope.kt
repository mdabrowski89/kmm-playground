package pl.mobite.playground.common

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun mainScope(): CoroutineScope = kotlinx.coroutines.MainScope()

fun cancel(scope: CoroutineScope) = scope.cancel()

/*
open class MainImmediateScope: CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    public fun onCleared() {
        cancel()
    }
}
*/