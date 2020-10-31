package pl.mobite.playground.ui.base

import android.os.Bundle
import pl.mobite.playground.common.mvi.MviEvent

class MviEventsCache(name: String) {

    private val mviEventsCache = HashSet<String>()
    private val mviEventsCacheKey = "${name}_MVI_EVENTS_CACHE_KEY"

    fun loadEvents(inBundle: Bundle?) {
        inBundle?.getStringArray(mviEventsCacheKey)?.let(mviEventsCache::addAll)
    }

    fun saveEvents(outBundle: Bundle?) {
        outBundle?.putStringArray(mviEventsCacheKey, mviEventsCache.toTypedArray())
    }

    /**
     * After consumption of single mvi event its id is added to a cache
     * in order to prevent further consumptions on the same Fragment
     */
    fun <T> consumeEvent(event: MviEvent<T>, action: (T) -> Unit) =
        with(event) {
            if (mviEventsCache.contains(id)) return@with
            action(value)
            mviEventsCache.add(id)
        }
}