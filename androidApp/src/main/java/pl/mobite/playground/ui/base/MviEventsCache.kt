package pl.mobite.playground.ui.base

import android.os.Bundle
import pl.mobite.playground.common.mvi.MviEvent

class MviEventsCache(name: String) {

    private val mviEventsCache = HashSet<String>()
    private val mviEventsCacheKey = "mvi.cache.events.${name}"

    fun loadFromBundle(bundle: Bundle?) {
        bundle?.getStringArray(mviEventsCacheKey)?.let(mviEventsCache::addAll)
    }

    fun saveInBundle(bundle: Bundle?) {
        bundle?.putStringArray(mviEventsCacheKey, mviEventsCache.toTypedArray())
    }

    /**
     * Simple scoped version of [consumeEvent].
     *
     * example:
     *  Use with(cache) { consume(event) }
     * */
    fun <T: Any> MviEvent<T>.consume(action: (T) -> Unit) = consumeEvent(this, action)

    /**
     * After consumption of single mvi event its id is added to a cache
     * in order to prevent further consumptions on the same Fragment
     */
    private fun <T: Any> consumeEvent(event: MviEvent<T>, action: (T) -> Unit) =
        with(event) {
            if (mviEventsCache.contains(id)) return@with
            action(value)
            mviEventsCache.add(id)
        }
}
