package pl.mobite.playground.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun doInitKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    modules(
        homeModule,
        taskUseCaseModule
    )
    appDeclaration()
}
