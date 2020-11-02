package pl.mobite.playground.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.safeCast

/**
 * MviEventsCache should be optional not required
 * */
//abstract class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId) {
//
//    private val mviEventsCache = MviEventsCache(javaClass.name)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mviEventsCache.loadFromBundle(savedInstanceState)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mviEventsCache.saveInBundle(outState)
//    }
//
//    protected fun <T> MviEvent<T>.consume(action: (T) -> Unit) = mviEventsCache.consumeEvent(this, action)
//}

abstract class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SaveStateListener::class.safeCast(this)?.load(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        SaveStateListener::class.safeCast(this)?.save(outState)
    }
}
