package pl.mobite.playground.di

import org.koin.dsl.module
import pl.mobite.playground.common.mvi.MviControllerProvider
import pl.mobite.playground.domain.home.mvi.HomeActionProcessingProvider
import pl.mobite.playground.domain.home.mvi.HomeControllerProvider
import pl.mobite.playground.domain.home.mvi.HomeResultProcessingProvider
import pl.mobite.playground.domain.home.mvi.impl.AddTaskActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.DeleteCompletedTasksActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeResultReducer
import pl.mobite.playground.domain.home.mvi.impl.LoadTasksActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.UpdateTaskActionProcessor

val homeModule = module {
    factory {
        HomeActionProcessor(
            loadTasksActionProcessor = LoadTasksActionProcessor(get()),
            addTaskActionProcessor = AddTaskActionProcessor(get()),
            updateTaskActionProcessor = UpdateTaskActionProcessor(get(), get()),
            deleteCompletedTasksActionProcessor = DeleteCompletedTasksActionProcessor(get(), get())
        )
    }

    factory {
        HomeActionProcessingProvider(homeActionProcessor = get())
    }

    factory {
        HomeResultProcessingProvider(homeResultReducer = get())
    }

    factory { HomeResultReducer() }

    factory {
        HomeControllerProvider(
            homeActionProcessingProvider = get(),
            homeResultProcessingProvider = get(),
        )
    }
}