package pl.mobite.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import pl.mobite.playground.di.*

class PlaygroundApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        doInitKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PlaygroundApp)
            modules(
                roomModule,
                serviceModule,
                viewModelModule,
                *platformModules
            )
        }
    }
}