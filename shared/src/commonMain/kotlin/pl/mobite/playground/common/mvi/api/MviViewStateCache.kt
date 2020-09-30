package pl.mobite.playground.common.mvi.api

interface MviViewStateCache<VS : MviViewState> {

    fun get(): VS?

    fun set(viewState: VS)
}