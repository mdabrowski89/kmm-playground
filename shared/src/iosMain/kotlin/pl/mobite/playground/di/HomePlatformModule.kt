package pl.mobite.playground.di

import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.HomeResultProcessing
import pl.mobite.playground.domain.home.mvi.impl.HomeViewStateCache

actual val homePlatformModule = module {

    factory {
        HomeViewStateCache()
    }

    factory {
        HomeResultProcessing(
            homeResultReducer = get(),
            homeViewStateCache = get()
        )
    }

    factory { (coroutineScope: CoroutineScope) ->
        HomeMviController(
            homeActionProcessing = get(),
            homeResultProcessing = get(),
            coroutineScope = coroutineScope
        )
    }
}