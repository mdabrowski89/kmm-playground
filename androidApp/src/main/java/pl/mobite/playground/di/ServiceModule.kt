package pl.mobite.playground.di

import org.koin.dsl.module
import pl.mobite.playground.data.service.TaskService
import pl.mobite.playground.data.service.TaskServiceAndroid

val serviceModule = module {
    factory<TaskService> { TaskServiceAndroid(get()) }
}