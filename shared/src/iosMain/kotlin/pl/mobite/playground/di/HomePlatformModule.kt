package pl.mobite.playground.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.dsl.module
import pl.mobite.playground.common.mvi.MviController
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.HomeResultProcessing
import pl.mobite.playground.domain.home.mvi.impl.HomeAction
import pl.mobite.playground.domain.home.mvi.impl.HomeResult
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
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

    factory {
        HomeMviController(
            homeActionProcessing = get(),
            homeResultProcessing = get(),
            coroutineScope = MainScope()
        )
    }
}