package pl.mobite.playground.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.mobite.playground.data.room.dao.TaskDao
import pl.mobite.playground.data.room.entities.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}