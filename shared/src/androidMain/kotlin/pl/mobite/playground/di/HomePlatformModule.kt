package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.HomeResultProcessing
import pl.mobite.playground.domain.home.mvi.impl.HomeViewStateCache

actual val homePlatformModule = module {

    factory { (savedStateHandle: SavedStateHandle) ->
        HomeViewStateCache(
            savedStateHandle = savedStateHandle
        )
    }

    factory { (savedStateHandle: SavedStateHandle) ->
        HomeResultProcessing(
            homeResultReducer = get(),
            homeViewStateCache = get { parametersOf(savedStateHandle) }
        )
    }

    factory { (savedStateHandle: SavedStateHandle, coroutineScope: CoroutineScope) ->
        HomeMviController(
            homeActionProcessing = get(),
            homeResultProcessing = get { parametersOf(savedStateHandle) },
            coroutineScope = coroutineScope
        )
    }
}