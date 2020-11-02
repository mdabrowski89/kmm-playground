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

    /**
     * I think it should be bound to either cache or reducer - this floating ViewState will become
     * a mess imho in future
     * */
//    factory { HomeViewState() }

    factory {
        HomeMviController(
            actionProcessor = get(),
            initialViewState = HomeViewState(),
            resultReducer = get(),
            coroutineScope = MainScope()
        )
    }
}