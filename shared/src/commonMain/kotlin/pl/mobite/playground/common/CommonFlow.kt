package pl.mobite.playground.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun <T> Flow<T>.asCommonFlow(coroutineScope: CoroutineScope): CommonFlow<T> = CommonFlow(coroutineScope, this)

/**
 * Wrapper around Flow which allows to consume it on iOS,
 * based on:
 * https://stackoverflow.com/questions/64175099/listen-to-kotlin-coroutine-flow-from-ios
 * https://github.com/JetBrains/kotlinconf-app/blob/master/common/src/mobileMain/kotlin/org/jetbrains/kotlinconf/FlowUtils.kt
 *
 * but with coroutine scope passed as an argument
 */

interface Closeable {
    fun close()
}

class CommonFlow<T>(
    private val coroutineScope: CoroutineScope,
    private val origin: Flow<T>
) : Flow<T> by origin {

    fun watch(block: (T) -> Unit): Closeable {
        onEach {
            block(it)
        }.launchIn(coroutineScope)

        return object: Closeable {
            override fun close() = coroutineScope.cancel()
        }
    }
}