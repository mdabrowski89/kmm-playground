package pl.mobite.playground.common.mvi.impl

import pl.mobite.playground.common.mvi.api.MviViewState
import pl.mobite.playground.common.mvi.api.MviViewStateCache

open class MviViewStateCacheIOS<VS: MviViewState> : MviViewStateCache<VS> {

    override fun get(): VS? {
        // TODO: implement
        return null
    }

    override fun set(viewState: VS) {
        // TODO: implement
    }
}