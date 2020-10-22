package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import org.koin.dsl.module
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.impl.mviViewStateCacheAndroid
import kotlin.reflect.KClass

actual val commonPlatformModule = module {
    factory { (savedStateHandle: SavedStateHandle, cls: KClass<MviViewState>) ->
        mviViewStateCacheAndroid(cls = cls, savedStateHandle = savedStateHandle)
    }
}