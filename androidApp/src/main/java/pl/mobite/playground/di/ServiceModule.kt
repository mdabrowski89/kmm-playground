package pl.mobite.playground.di

import org.koin.core.module.Module
import pl.mobite.playground.data.service.TaskService
import pl.mobite.playground.data.service.TaskServiceAndroid

inline val Module.ServiceModule
    get() = configure {
        factory<TaskService> { TaskServiceAndroid(get()) }
    }