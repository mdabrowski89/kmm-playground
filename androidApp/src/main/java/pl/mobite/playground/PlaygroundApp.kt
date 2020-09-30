package pl.mobite.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mobite.playground.di.appModule

class PlaygroundApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@PlaygroundApp)
            modules(
                appModule
            )
        }
    }
}