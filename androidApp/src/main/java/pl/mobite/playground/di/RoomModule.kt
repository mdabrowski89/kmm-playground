package pl.mobite.playground.di

import androidx.room.Room
import org.koin.dsl.module
import pl.mobite.playground.data.room.AppDatabase

val roomModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { get<AppDatabase>().taskDao() }
}