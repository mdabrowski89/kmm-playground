package pl.mobite.playground.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import pl.mobite.playground.common.mvi.MviEvent

abstract class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    private val mviEventsCache = MviEventsCache(javaClass.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviEventsCache.loadFromBundle(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mviEventsCache.saveInBundle(outState)
    }

    protected fun <T: Any> MviEvent<T>.consume(action: (T) -> Unit) = mviEventsCache.consumeEvent(this, action)
}
