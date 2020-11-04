package pl.mobite.playground.di

import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.*

val homeModule = module {

    factory {
        HomeActionProcessing(
            loadTasksActionProcessor = LoadTasksActionProcessor(get()),
            addTaskActionProcessor = AddTaskActionProcessor(get()),
            updateTaskActionProcessor = UpdateTaskActionProcessor(get(), get()),
            deleteCompletedTasksActionProcessor = DeleteCompletedTasksActionProcessor(get(), get())
        )
    }

    factory { HomeResultReducer() }

    factory { (initialState: HomeViewState, coroutineScope: CoroutineScope) ->
        HomeMviController(
            actionProcessing = get(),
            resultReducer = get(),
            initialViewState = initialState,
            coroutineScope = coroutineScope
        )
    }
}