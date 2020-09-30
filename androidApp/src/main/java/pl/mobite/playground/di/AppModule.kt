package pl.mobite.playground.di

import org.koin.dsl.module

val appModule = module {
    RoomModule
    ServiceModule
    TaskUseCaseModule
    HomeModule
}