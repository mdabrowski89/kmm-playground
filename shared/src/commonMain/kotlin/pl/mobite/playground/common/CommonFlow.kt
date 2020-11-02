package pl.mobite.playground.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun <T: Any> Flow<T>.asCommonFlow(coroutineScope: CoroutineScope): CommonFlow<T> = CommonFlow(coroutineScope, this)

/**
 * Wrapper around Flow. It allows to consume flow results on iOS, and it is also used in MviViewStateCache
 * based on:
 * https://stackoverflow.com/questions/64175099/listen-to-kotlin-coroutine-flow-from-ios
 * https://github.com/JetBrains/kotlinconf-app/blob/master/common/src/mobileMain/kotlin/org/jetbrains/kotlinconf/FlowUtils.kt
 *
 * but with coroutine scope passed as an argument
 */
class CommonFlow<T: Any>(
    private val coroutineScope: CoroutineScope,
    private val origin: Flow<T>
) : Flow<T> by origin {

    fun watch(block: (T) -> Unit) {
        onEach {
            block(it)
        }.launchIn(coroutineScope)
    }
}