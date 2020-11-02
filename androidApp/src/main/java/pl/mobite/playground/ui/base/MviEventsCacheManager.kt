package pl.mobite.playground.ui.base

import android.os.Bundle

interface MviEventsCacheManager : SaveStateListener {
    val cache: MviEventsCache

    override fun load(bundle: Bundle?) = cache.loadFromBundle(bundle)
    override fun save(bundle: Bundle?) = cache.saveInBundle(bundle)
}