package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.mvi.MviEvent
import pl.mobite.playground.common.mvi.api.MviResultReducer
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.AddTaskResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.ErrorResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.InProgressResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.TasksListUpdatedResult

/**
 * Reducer which creates new ViewState based on the last ViewState and new Result
 */
class HomeResultReducer : MviResultReducer<HomeResult, HomeViewState> {

    override fun HomeViewState.reduce(result: HomeResult): HomeViewState {
        return when (result) {
            is InProgressResult -> reduce(result)
            is ErrorResult -> reduce(result)
            is TasksListUpdatedResult -> reduce(result)
            is AddTaskResult -> reduce(result)
        }
    }

    private fun HomeViewState.reduce(@Suppress("UNUSED_PARAMETER") result: InProgressResult) = copy(
        inProgress = true
    )

    private fun HomeViewState.reduce(result: ErrorResult) = copy(
        inProgress = false,
        errorEvent = MviEvent.create(result.t)
    )

    private fun HomeViewState.reduce(result: TasksListUpdatedResult) = copy(
        inProgress = false,
        tasks = result.tasks
    )

    private fun HomeViewState.reduce(result: AddTaskResult) = copy(
        inProgress = false,
        taskAddedEvent = MviEvent.create(true)
    )
}
