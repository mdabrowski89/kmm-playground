package pl.mobite.playground.ui.base

import androidx.lifecycle.SavedStateHandle
import pl.mobite.playground.common.mvi.MviEvent

class MviEventsCache(name: String) {

    private val mviEventsCache = HashSet<String>()
    private val mviEventsCacheKey = "mvi.cache.events.${name}"

    fun load(savedStateHandle: SavedStateHandle) {
        savedStateHandle.get<Array<String>>(mviEventsCacheKey)?.let(mviEventsCache::addAll)
    }

    fun save(savedStateHandle: SavedStateHandle) {
        savedStateHandle[mviEventsCacheKey] = mviEventsCache.toTypedArray()
    }

    /**
     * After consumption of single mvi event its id is added to a cache
     * in order to prevent further consumptions on the same Fragment
     */
    fun <T: Any> consumeEvent(event: MviEvent<T>?, action: (T) -> Unit) {
        if (event == null) return
        with(event) {
            if (mviEventsCache.contains(id)) return@with
            action(value)
            mviEventsCache.add(id)
        }
    }
}
