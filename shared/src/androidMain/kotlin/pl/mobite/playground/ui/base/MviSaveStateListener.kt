package pl.mobite.playground.ui.base

import android.os.Bundle

interface MviSaveStateListener{
    fun load(bundle: Bundle?)
    fun save(bundle: Bundle?)
}
