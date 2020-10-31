package pl.mobite.playground.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.mobite.playground.common.mvi.MviEvent

abstract class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    private val mviEventsCache = HashSet<String>()
    private val mviEventsCacheKey = "${javaClass.name}_MVI_EVENTS_CACHE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getStringArray(mviEventsCacheKey)?.let(mviEventsCache::addAll)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(mviEventsCacheKey, mviEventsCache.toTypedArray())
    }

    /**
     * After consumption of single mvi event its id is added to a cache
     * in order to prevent further consumptions on the same Fragment
     */
    protected fun <T> MviEvent<T>.consume(action: (T) -> Unit) = with(this) {
        if (mviEventsCache.contains(id)) return@with
        action(value)
        mviEventsCache.add(id)
    }
}
