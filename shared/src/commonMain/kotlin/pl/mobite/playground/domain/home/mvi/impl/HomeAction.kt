package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.mvi.api.MviAction

/**
 * Set of action which could be done in Home feature
 */
sealed class HomeAction : MviAction {

    object ObserveTasksUpdatesAction : HomeAction()

    data class AddTaskAction(val taskContent: String) : HomeAction()

    data class UpdateTaskAction(val taskId: Long, val isDone: Boolean) : HomeAction() {
        override fun getId() = super.getId().toString() + taskId
    }

    object DeleteCompletedTasksAction : HomeAction()
}