package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.mvi.api.MviResultReducer
import pl.mobite.playground.common.ViewStateEmptyEvent
import pl.mobite.playground.common.ViewStateErrorEvent
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.AddTaskResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.DeleteCompletedTasksResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.ErrorResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.EventConsumption.ErrorConsumed
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.EventConsumption.NewTaskAddedConsumed
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.InProgressResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.LoadTasksResult
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.UpdateTaskResult

/**
 * Reducer which creates new ViewState based on the last ViewState and new Result
 */
class HomeResultReducer : MviResultReducer<HomeResult, HomeViewState> {

    override fun HomeViewState.reduce(result: HomeResult): HomeViewState {
        return when (result) {
            is InProgressResult -> reduce(result)
            is ErrorResult -> reduce(result)
            is LoadTasksResult -> reduce(result)
            is AddTaskResult -> reduce(result)
            is UpdateTaskResult -> reduce(result)
            is DeleteCompletedTasksResult -> reduce(result)
            is NewTaskAddedConsumed -> reduce(result)
            is ErrorConsumed -> reduce(result)
        }
    }

    override fun default(): HomeViewState =
        HomeViewState(
            inProgress = false,
            tasks = null,
            newTaskAdded = null,
            error = null
        )

    private fun HomeViewState.reduce(@Suppress("UNUSED_PARAMETER") result: InProgressResult) =
        copy(inProgress = true)

    private fun HomeViewState.reduce(result: ErrorResult) =
        copy(inProgress = false, error = result.t)

    private fun HomeViewState.reduce(result: LoadTasksResult) =
        copy(inProgress = false , tasks = result.tasks)

    private fun HomeViewState.reduce(result: AddTaskResult) =
        copy(
            inProgress = false,
            tasks = tasks?.toMutableList()?.apply { add(result.addedTask) }?.toList(),
            newTaskAdded = true
        )

    private fun HomeViewState.reduce(result: UpdateTaskResult) =
        copy(
            inProgress = false,
            tasks = tasks?.map {
                if (it.id == result.updatedTask.id) result.updatedTask else it
            } // replace updated task in the tasks list
        )

    private fun HomeViewState.reduce(result: DeleteCompletedTasksResult) =
        copy(
            inProgress = false,
            tasks = with(result.deletedTasks.map { it.id }) {
                tasks?.filterNot { it.id in this }
            }
        )

    private fun HomeViewState.reduce(result: NewTaskAddedConsumed) =
        copy(newTaskAdded = null)

    private fun HomeViewState.reduce(result: ErrorConsumed) =
        copy(error = null)
}
