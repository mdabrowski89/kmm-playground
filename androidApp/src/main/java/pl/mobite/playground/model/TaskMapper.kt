package pl.mobite.playground.model

import pl.mobite.playground.data.room.entities.TaskEntity

fun Task.toTaskEntity() = TaskEntity(
    id,
    content,
    isDone
)

fun TaskEntity.toTask() = Task(
    id,
    content,
    isDone
)