package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
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

    factory(override = true) { (initialState: HomeViewState, coroutineScope: CoroutineScope) ->
        HomeMviController(
            actionProcessor = get(),
            initialViewState = initialState,
            resultReducer = get(),
            coroutineScope = coroutineScope
        )
    }
}