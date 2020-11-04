package pl.mobite.playground.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.safeCast

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
