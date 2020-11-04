package pl.mobite.playground.di

import kotlinx.coroutines.CoroutineScope
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

    factory { (initialState: HomeViewState, coroutineScope: CoroutineScope) ->
        HomeMviController(
            actionProcessor = get(),
            resultReducer = get(),
            initialViewState = initialState,
            coroutineScope = coroutineScope
        )
    }
}