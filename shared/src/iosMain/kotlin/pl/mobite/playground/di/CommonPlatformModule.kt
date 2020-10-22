package pl.mobite.playground.di

import org.koin.dsl.module
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.impl.mviViewStateCacheIOS
import kotlin.reflect.KClass

actual val commonPlatformModule = module {

    factory { (cls: KClass<MviViewState>) -> mviViewStateCacheIOS(cls) }
}