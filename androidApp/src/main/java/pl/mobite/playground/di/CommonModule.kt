package pl.mobite.playground.di

import org.koin.core.module.Module

inline fun Module.configure(configuration: Module.() -> Unit) {
    configuration(this)
}