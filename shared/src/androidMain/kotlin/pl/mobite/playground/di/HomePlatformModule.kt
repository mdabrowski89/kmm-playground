package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeViewStateCache

actual val homePlatformModule = module {

    factory { (savedStateHandle: SavedStateHandle) ->
        HomeViewStateCache(
            savedStateHandle = savedStateHandle
        )
    }

    /* todo: same strategy as with processingFlow and resultFlow
    *   using HomeMviControllerProvider gives us benefits of providing only configuration
    *   necessary to wire up all the ends by ie. ViewModel on its own
    *   this makes dependencies of mviFlow opaque for platform configuration
    * */
    factory { (savedStateHandle: SavedStateHandle, coroutineScope: CoroutineScope) ->
        HomeMviController(
            homeActionProcessing = get(),
            homeResultProcessing = get { parametersOf(savedStateHandle) },
            cache = get { parametersOf(savedStateHandle) },
            coroutineScope = coroutineScope
        )
    }
}