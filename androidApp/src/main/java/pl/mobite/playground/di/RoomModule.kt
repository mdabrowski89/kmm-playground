package pl.mobite.playground.di

import androidx.room.Room
import org.koin.core.module.Module
import pl.mobite.playground.data.room.AppDatabase

inline val Module.RoomModule
    get() = configure {
        single {
            Room.databaseBuilder(get(), AppDatabase::class.java, "app-database")
                .fallbackToDestructiveMigration()
                .build()
        }

        factory { get<AppDatabase>().taskDao() }
    }