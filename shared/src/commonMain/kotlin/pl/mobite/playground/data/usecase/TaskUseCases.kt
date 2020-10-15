@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package pl.mobite.playground.data.usecase

import pl.mobite.playground.common.SuspendableUseCase0
import pl.mobite.playground.common.SuspendableUseCase1
import pl.mobite.playground.common.SuspendableUseCase1n
import pl.mobite.playground.data.service.TaskService
import pl.mobite.playground.model.Task

interface AddTaskUseCase : SuspendableUseCase1<Task, Task>
interface GetTaskUseCase : SuspendableUseCase1<Long, Task?>
interface GetAllTasksUseCase : SuspendableUseCase0<List<Task>>
interface GetAllDoneTasksUseCase : SuspendableUseCase0<List<Task>>
interface UpdateTaskUseCase : SuspendableUseCase1n<Task>
interface DeleteTasksUseCase : SuspendableUseCase1n<List<Task>>

class AddTaskUseCaseImpl(
    private val taskService: TaskService
) : AddTaskUseCase {

    override suspend fun invoke(task: Task): Task {
        val id = taskService.insert(task)
        return task.copy(id = id)
    }
}

class GetTaskUseCaseImpl(
    private val taskService: TaskService
) : GetTaskUseCase {

    override suspend fun invoke(taskId: Long): Task? {
        return taskService
            .getForId(taskId)
            .firstOrNull()
    }
}

class GetAllTasksUseCaseImpl(
    private val taskService: TaskService
) : GetAllTasksUseCase {

    override suspend fun invoke(): List<Task> {
        return taskService
            .getAll()
    }
}

class GetAllDoneTasksUseCaseImpl(
    private val taskService: TaskService
) : GetAllDoneTasksUseCase {

    override suspend fun invoke(): List<Task> {
        return taskService
            .getAllDone()
    }
}

class UpdateTaskUseCaseImpl(
    private val taskService: TaskService
) : UpdateTaskUseCase {

    override suspend fun invoke(task: Task) {
        taskService.update(task)
    }
}

class DeleteTasksUseCaseImpl(
    private val taskService: TaskService
) : DeleteTasksUseCase {

    override suspend fun invoke(tasks: List<Task>) {
        taskService.delete(tasks)
    }
}