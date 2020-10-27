package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.mvi.api.MviResult
import pl.mobite.playground.model.Task

/**
 * Set of results which could be returned after processing Home feature Action
 */
sealed class HomeResult : MviResult {

    object InProgressResult : HomeResult()

    data class ErrorResult(val t: Throwable) : HomeResult()

    data class LoadTasksResult(val tasks: List<Task>) : HomeResult()

    sealed class InternalResults : HomeResult() {
        object ReloadResult : InternalResults()
    }

    sealed class EventConsumption : HomeResult() {

        object ErrorConsumed : EventConsumption()

        object NewTaskAddedConsumed : EventConsumption()
    }
}