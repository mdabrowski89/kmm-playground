package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import pl.mobite.playground.domain.home.HomeViewStateCache
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState

val homePlatformModule = module {

    factory { (savedStateHandle: SavedStateHandle) ->
        HomeViewStateCache(
            savedStateHandle = savedStateHandle
        )
    }

    factory(override = true) { (savedStateHandle: SavedStateHandle, coroutineScope: CoroutineScope) ->
        val homeViewStateCache: HomeViewStateCache = get { parametersOf(savedStateHandle) }
        HomeMviController(
            actionProcessor = get(),
            initialViewState = homeViewStateCache.get() ?: HomeViewState(),
            resultReducer = get(),
            coroutineScope = coroutineScope
        ).apply {
            homeViewStateCache.useWith(viewStatesFlow).launchIn(coroutineScope)
        }
    }
}