package pl.mobite.playground.di

import org.koin.dsl.module
import pl.mobite.playground.data.usecase.AddTaskUseCase
import pl.mobite.playground.data.usecase.AddTaskUseCaseImpl
import pl.mobite.playground.data.usecase.DeleteTasksUseCase
import pl.mobite.playground.data.usecase.DeleteTasksUseCaseImpl
import pl.mobite.playground.data.usecase.GetAllDoneTasksUseCase
import pl.mobite.playground.data.usecase.GetAllDoneTasksUseCaseImpl
import pl.mobite.playground.data.usecase.GetAllTasksUseCase
import pl.mobite.playground.data.usecase.GetAllTasksUseCaseImpl
import pl.mobite.playground.data.usecase.GetTaskUseCase
import pl.mobite.playground.data.usecase.GetTaskUseCaseImpl
import pl.mobite.playground.data.usecase.UpdateTaskUseCase
import pl.mobite.playground.data.usecase.UpdateTaskUseCaseImpl

val taskUseCaseModule = module {
    factory<AddTaskUseCase> { AddTaskUseCaseImpl(get()) }
    factory<GetTaskUseCase> { GetTaskUseCaseImpl(get()) }
    factory<GetAllTasksUseCase> { GetAllTasksUseCaseImpl(get()) }
    factory<GetAllDoneTasksUseCase> { GetAllDoneTasksUseCaseImpl(get()) }
    factory<UpdateTaskUseCase> { UpdateTaskUseCaseImpl(get()) }
    factory<DeleteTasksUseCase> { DeleteTasksUseCaseImpl(get()) }
}
