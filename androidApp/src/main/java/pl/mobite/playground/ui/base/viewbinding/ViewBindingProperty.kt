package pl.mobite.playground.ui.base.viewbinding

import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingProperty<in R : Any, T : ViewBinding>(
    private val viewBinder: ViewBindingProperty<R, T>.(R) -> T
) : ReadOnlyProperty<R, T> {

    private var viewBinding: T? = null

    override fun getValue(thisRef: R, property: KProperty<*>): T =
        viewBinding ?: attachBinding(thisRef)

    @PublishedApi
    internal fun attachBinding(thisRef: R): T =
        viewBinder(thisRef).also { viewBinding = it }

    @PublishedApi
    internal fun detachBinding() {
        viewBinding = null
    }
}