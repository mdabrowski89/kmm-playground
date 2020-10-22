package pl.mobite.playground.common.mvi.impl

import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.api.MviViewStateCache
import kotlin.reflect.KClass

open class MviViewStateCacheIOS<VS : MviViewState> : MviViewStateCache<VS> {

    override fun get(): VS? {
        // TODO: implement
        return null
    }

    override fun set(viewState: VS) {
        // TODO: implement
    }
}


/**
 * [MviViewStateCacheIOS] builder
 * */
@Suppress("NOTHING_TO_INLINE", "UNUSED_PARAMETER")
inline fun <VS : MviViewState> mviViewStateCacheIOS(
    cls: KClass<VS>,
): MviViewStateCache<VS> = MviViewStateCacheIOS()

