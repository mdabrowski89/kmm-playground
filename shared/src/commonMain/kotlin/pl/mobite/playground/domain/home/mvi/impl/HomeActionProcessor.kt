package pl.mobite.playground.domain.home.mvi.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pl.mobite.playground.common.mvi.api.MviActionProcessor
import pl.mobite.playground.data.usecase.*
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.*
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.*
import pl.mobite.playground.model.Task
import kotlin.random.Random

/** delay processing of na action - only for test purposes */
private const val PROCESSING_DELAY_MILS = 500L

/**
 * "Specialized" processors. Each of them is responsible for processing different action
 * and each of them depends on the UseCases. This is also a convention and it could be implemented
 * differently. For example, processors could depends on repositories or services.
 */

class LoadTasksActionProcessor(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : MviActionProcessor<LoadTasksAction, HomeResult> {

    override fun invoke(action: LoadTasksAction) = flow {
        emit(InProgressResult)
        delay(PROCESSING_DELAY_MILS)
        emit(LoadTasksResult(getAllTasksUseCase()))
    }.catch {
        emit(ErrorResult(it))
    }
}

class AddTaskActionProcessor(
    private val addTaskUseCase: AddTaskUseCase
) : MviActionProcessor<AddTaskAction, HomeResult> {

    override fun invoke(action: AddTaskAction) = flow {
        emit(InProgressResult)

        delay(PROCESSING_DELAY_MILS)
        addTaskUseCase(Task(Random.nextLong(), action.taskContent, false))

        emit(InternalResults.ReloadResult)
    }.catch {
        emit(ErrorResult(it))
    }
}

class UpdateTaskActionProcessor(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : MviActionProcessor<UpdateTaskAction, HomeResult> {

    override fun invoke(action: UpdateTaskAction) = flow {
        emit(InProgressResult)

        delay(PROCESSING_DELAY_MILS)
        val task = getTaskUseCase(action.taskId)?.copy(isDone = action.isDone)
            ?: throw Exception("Task with id ${action.taskId} not found in DB")
        updateTaskUseCase(task)

        emit(InternalResults.ReloadResult)
    }.catch {
        emit(ErrorResult(it))
    }
}

class DeleteCompletedTasksActionProcessor(
    private val getAllDoneTasksUseCase: GetAllDoneTasksUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase
) : MviActionProcessor<DeleteCompletedTasksAction, HomeResult> {

    override fun invoke(action: DeleteCompletedTasksAction) = flow {
        emit(InProgressResult)

        delay(PROCESSING_DELAY_MILS)
        deleteTasksUseCase(getAllDoneTasksUseCase())

        emit(InternalResults.ReloadResult)
    }.catch {
        emit(ErrorResult(it))
    }
}
