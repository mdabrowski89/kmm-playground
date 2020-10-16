package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.Parcelize
import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.ViewStateEmptyEvent
import pl.mobite.playground.common.ViewStateErrorEvent
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.AddTaskAction
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.DeleteCompletedTasksAction
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.LoadTasksAction
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.UpdateTaskAction
import pl.mobite.playground.model.Task

/**
 * Describes the UI state of the Home feature - fragment is using it to render the views
 */
@Parcelize
data class HomeViewState(
    val inProgress: Boolean,
    val tasks: List<Task>?,
    val newTaskAdded: Boolean?,
    val error: Throwable?,
) : MviViewState {

    override fun isSavable(): Boolean = !inProgress

    fun loadDataIfNeeded(): HomeAction? {
        return if (tasks == null) LoadTasksAction else null
    }

    fun addTask(taskContent: String): HomeAction? {
        return if (!taskContent.isBlank()) AddTaskAction(taskContent) else null
    }

    fun deleteCompletedTasks(): HomeAction? {
        return DeleteCompletedTasksAction
    }

    fun updateTask(taskId: Long, isDone: Boolean): HomeAction? {
        return UpdateTaskAction(taskId, isDone)
    }
}