package pl.mobite.playground.utils

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.androidx.viewmodel.ext.android.getStateViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.ui.base.MviEventsCache
import kotlin.reflect.KClass

/**
 * Instantiate ViewModel and provides an object reference from this ViewModel
 */
inline fun <reified VM : ViewModel, reified T : Any> SavedStateRegistryOwner.provideFrom(
    clazz: KClass<VM>,
    qualifier: Qualifier? = null,
    bundle: Bundle? = null,
    noinline parameters: ParametersDefinition? = null,
    crossinline provider: VM.() -> T
): Lazy<T> {
    return lazy { getStateViewModel(clazz, qualifier, bundle, parameters).provider() }
}

