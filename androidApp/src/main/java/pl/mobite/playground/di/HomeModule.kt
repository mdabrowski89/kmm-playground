package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.domain.home.mvi.HomeActionProcessing
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.HomeResultProcessing
import pl.mobite.playground.domain.home.mvi.impl.AddTaskActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.DeleteCompletedTasksActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.HomeResultReducer
import pl.mobite.playground.domain.home.mvi.impl.HomeViewStateCache
import pl.mobite.playground.domain.home.mvi.impl.LoadTasksActionProcessor
import pl.mobite.playground.domain.home.mvi.impl.UpdateTaskActionProcessor
import pl.mobite.playground.ui.components.home.HomeViewModel

inline val Module.HomeModule
    get() = configure {
        factory {
            HomeActionProcessor(
                loadTasksActionProcessor = LoadTasksActionProcessor(get()),
                addTaskActionProcessor = AddTaskActionProcessor(get()),
                updateTaskActionProcessor = UpdateTaskActionProcessor(get(), get()),
                deleteCompletedTasksActionProcessor = DeleteCompletedTasksActionProcessor(get(), get())
            )
        }

        factory {
            HomeActionProcessing(
                homeActionProcessor = get()
            )
        }

        factory { HomeResultReducer() }

        factory { (savedStateHandle: SavedStateHandle) ->
            HomeViewStateCache(
                savedStateHandle = savedStateHandle
            )
        }

        factory { (savedStateHandle: SavedStateHandle) ->
            HomeResultProcessing(
                homeResultReducer = get(),
                homeViewStateCache = get { parametersOf(savedStateHandle) }
            )
        }

        factory { (savedStateHandle: SavedStateHandle, coroutineScope: CoroutineScope) ->
            HomeMviController(
                homeActionProcessing = get(),
                homeResultProcessing = get { parametersOf(savedStateHandle) },
                coroutineScope = coroutineScope
            )
        }

        viewModel { (savedStateHandle: SavedStateHandle) ->
            HomeViewModel(savedStateHandle = savedStateHandle)
        }
    }