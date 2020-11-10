package pl.mobite.playground.domain.home.mvi.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import pl.mobite.playground.common.mvi.api.MviActionProcessor
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.data.usecase.*
import pl.mobite.playground.domain.home.mvi.impl.HomeAction.*
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.*
import pl.mobite.playground.model.Task
import kotlin.random.Random

/**
 * MviActionProcessor which delegates processing of each action to "specialized" processor.
 * This is only the conventions and it could be implemented differently.
 *
 * "specialized" processors have the same interface as main processor and they are extracted
 * to separate classes because each of them has different dependencies and it is responsible
 * for processing different action.
 */
class HomeActionProcessing(
    private val observeTasksUpdatesActionProcessor: ObserveTasksUpdatesActionProcessor,
    private val addTaskActionProcessor: AddTaskActionProcessor,
    private val updateTaskActionProcessor: UpdateTaskActionProcessor,
    private val deleteCompletedTasksActionProcessor: DeleteCompletedTasksActionProcessor
) : MviActionProcessing<HomeAction, HomeResult>() {

    override fun process(action: HomeAction) = when (action) {
        is ObserveTasksUpdatesAction -> observeTasksUpdatesActionProcessor(action)
        is AddTaskAction -> addTaskActionProcessor(action)
        is UpdateTaskAction -> updateTaskActionProcessor(action)
        is DeleteCompletedTasksAction -> deleteCompletedTasksActionProcessor(action)
    }
}

/** delay processing of na action - only for test purposes */
private const val PROCESSING_DELAY_MILS = 500L

/**
 * "Specialized" processors. Each of them is responsible for processing different action
 * and each of them depends on the UseCases. This is also a convention and it could be implemented
 * differently. For example, processors could depends on repositories or services.
 */

class ObserveTasksUpdatesActionProcessor(
    private val getAllTasksFlowUseCase: GetAllTasksFlowUseCase
) : MviActionProcessor<ObserveTasksUpdatesAction, HomeResult> {

    override fun invoke(action: ObserveTasksUpdatesAction) = flow {
        emit(InProgressResult)
        delay(PROCESSING_DELAY_MILS)
        getAllTasksFlowUseCase().onEach {
            this.emit(TasksListUpdatedResult(it))
        }.collect()
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
        emit(AddTaskResult)
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
        if (task == null) {
            emit(ErrorResult(Exception("Task with id ${action.taskId} not found in DB")))
        } else {
            updateTaskUseCase(task)
            // UI will be notified by the observing the tasks list
        }
    }.catch {
        emit(ErrorResult(it))
    }
}

class DeleteCompletedTasksActionProcessor(
    private val getAllDoneTasksUseCase: GetAllDoneTasksUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase
) : MviActionProcessor<DeleteCompletedTasksAction, HomeResult> {

    override fun invoke(action: DeleteCompletedTasksAction) = flow<HomeResult> {
        emit(InProgressResult)
        delay(PROCESSING_DELAY_MILS)
        deleteTasksUseCase(getAllDoneTasksUseCase())
        // UI will be notified by the observing the tasks list
    }.catch {
        emit(ErrorResult(it))
    }
}
