package pl.mobite.playground.di

import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeActionProcessing
import pl.mobite.playground.domain.home.mvi.HomeControllerProvider
import pl.mobite.playground.domain.home.mvi.HomeResultProcessingProvider
import pl.mobite.playground.domain.home.mvi.impl.*

val homeModule = module {
    factory { LoadTasksActionProcessor(get()) }
    factory { AddTaskActionProcessor(get()) }
    factory { UpdateTaskActionProcessor(get(), get()) }
    factory { DeleteCompletedTasksActionProcessor(get(), get()) }

    factory {
        HomeActionProcessing(get(), get(), get(), get())
    }

    factory {
        HomeResultProcessingProvider(homeResultReducer = get())
    }

    factory { HomeResultReducer() }

    factory {
        HomeControllerProvider(
            homeActionProcessing = get(),
            homeResultProcessingProvider = get(),
        )
    }
}