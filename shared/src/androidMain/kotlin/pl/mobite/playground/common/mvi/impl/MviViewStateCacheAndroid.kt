package pl.mobite.playground.common.mvi.impl

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import kotlin.reflect.KClass

open class MviViewStateCacheAndroid<VS : MviViewState>(
    private val key: String,
    private val savedStateHandle: SavedStateHandle
) : MviViewStateCache<VS> {

    override fun get() = savedStateHandle.get<VS>(key)

    override fun set(viewState: VS) = savedStateHandle.set(key, viewState)
}

/**
 * [MviViewStateCacheAndroid] builder which extracts
 * [MviViewStateCacheAndroid.key] from [KClass.qualifiedName]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <VS : MviViewState> mviViewStateCacheAndroid(
    cls: KClass<VS>,
    savedStateHandle: SavedStateHandle,
): MviViewStateCache<VS> = MviViewStateCacheAndroid(
    key = cls.toString(),
    savedStateHandle = savedStateHandle
)
