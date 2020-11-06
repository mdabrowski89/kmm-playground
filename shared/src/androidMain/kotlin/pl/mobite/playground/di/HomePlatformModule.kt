package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import org.koin.dsl.module
import pl.mobite.playground.ui.components.home.HomeViewStateCache

val homePlatformModule = module {

    factory { (savedStateHandle: SavedStateHandle) ->
        HomeViewStateCache(
            savedStateHandle = savedStateHandle
        )
    }
}