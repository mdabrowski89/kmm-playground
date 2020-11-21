package pl.mobite.playground.ui.base.viewbinding

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass
import kotlin.reflect.cast

class FragmentViewBinder<T : ViewBinding>(private val viewBindingClass: KClass<T>) {

    /**
     * Cache static method `ViewBinding.bind(View)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.java.getMethod("bind", View::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    fun bind(fragment: Fragment): T = viewBindingClass.cast(
        bindViewMethod(null, fragment.requireView())
    )

}

class FragmentInflateViewBinder<T : ViewBinding>(
    private val viewBindingClass: KClass<T>
) {

    /**
     * Cache static method `ViewBinding.inflate(LayoutInflater)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.java.getMethod("inflate", LayoutInflater::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    fun bind(fragment: Fragment): T = viewBindingClass.cast(
        bindViewMethod(null, fragment.layoutInflater)
    )
}

inline fun <reified T : ViewBinding> obtainViewBinder(
    createMethod: CreateMethod = CreateMethod.BIND
): (Fragment) -> T = when (createMethod) {
    CreateMethod.BIND -> FragmentViewBinder(T::class)::bind
    CreateMethod.INFLATE -> FragmentInflateViewBinder(T::class)::bind
}

/**
 * Create new [ViewBinding] associated with the [Fragment]
 *
 * @param T Class of expected [ViewBinding] result class
 */
@JvmName("viewBindingFragment")
inline fun <reified T : ViewBinding> Fragment.viewBinding(
    createMethod: CreateMethod = CreateMethod.BIND
): ViewBindingProperty<Fragment, T> {
    val handler = Handler(Looper.getMainLooper())
    val binder = obtainViewBinder<T>(createMethod)

    return ViewBindingProperty { owner ->
        owner.viewLifecycleOwner.lifecycle.addObserver(
            OnDestroyLifecycleObserver { handler.post(::detachBinding) }
        )

        binder(owner)
    }
}