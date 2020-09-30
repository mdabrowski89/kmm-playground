package pl.mobite.playground.data.service

import pl.mobite.playground.data.room.dao.TaskDao
import pl.mobite.playground.model.Task
import pl.mobite.playground.model.toTask
import pl.mobite.playground.model.toTaskEntity

class TaskServiceAndroid(
    private val taskDao: TaskDao
) : TaskService {

    override suspend fun getAll() = taskDao.getAll().map { it.toTask() }

    override suspend fun getAllDone() = taskDao.getAllDone().map { it.toTask() }

    override suspend fun getForId(id: Long) = taskDao.getForId(id).map { it.toTask() }

    override suspend fun count() = taskDao.count()

    override suspend fun insert(task: Task) = taskDao.insert(task.toTaskEntity())

    override suspend fun update(task: Task) = taskDao.update(task.toTaskEntity())

    override suspend fun delete(tasks: List<Task>) = taskDao.delete(tasks.map { it.toTaskEntity() })

    override suspend fun deleteAll() = taskDao.deleteAll()
}