package pl.mobite.playground.ui.base

import android.os.Bundle

interface SaveStateListener{
    fun load(bundle: Bundle?)
    fun save(bundle: Bundle?)
}
