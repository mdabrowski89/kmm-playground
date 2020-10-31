package pl.mobite.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import pl.mobite.playground.di.doInitKoin
import pl.mobite.playground.di.platformModules
import pl.mobite.playground.di.roomModule
import pl.mobite.playground.di.serviceModule
import pl.mobite.playground.di.viewModelModule

class PlaygroundApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        doInitKoin {
            androidContext(this@PlaygroundApp)
            modules(
                roomModule,
                serviceModule,
                viewModelModule,
                platformModules
            )
        }
    }
}