package pl.mobite.playground.ui.base

import android.os.Bundle
import pl.mobite.playground.common.mvi.MviEvent

interface MviEventsCacheManager : MviSaveStateListener {

    val cache: MviEventsCache

    override fun load(bundle: Bundle?) = cache.loadFromBundle(bundle)
    override fun save(bundle: Bundle?) = cache.saveInBundle(bundle)

    /**
     * Simple scoped version of [MviEventsCache.consumeEvent].
     *
     * example:
     *  event?.consume { ... }
     */
    fun <T: Any> MviEvent<T>.consume(action: (T) -> Unit) = cache.consumeEvent(this, action)
}