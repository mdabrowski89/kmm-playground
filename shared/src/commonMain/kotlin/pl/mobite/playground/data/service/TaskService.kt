package pl.mobite.playground.data.service

import pl.mobite.playground.model.Task

interface TaskService {

    suspend fun getAll(): List<Task>

    suspend fun getAllDone(): List<Task>

    suspend fun getForId(id: Long): List<Task>

    suspend fun count(): Int

    suspend fun insert(task: Task): Long

    suspend fun update(task: Task)

    suspend fun delete(tasks: List<Task>)

    suspend fun deleteAll()
}