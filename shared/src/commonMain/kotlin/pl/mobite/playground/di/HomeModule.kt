package pl.mobite.playground.di

import kotlinx.coroutines.MainScope
import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.AddTaskActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.DeleteCompletedTasksActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeResultReducer
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState
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

    factory { HomeResultReducer() }

    factory { HomeViewState() }

    factory {
        HomeMviController(
            actionProcessor = get(),
            initialViewState = get(),
            resultReducer = get(),
            coroutineScope = MainScope()
        )
    }
}