package pl.mobite.playground.common.mvi.api

/**
 * Describes how one MviResult object instance is reduced with current [MviViewState].
 */
interface MviResultReducer<R : MviResult, VS : MviViewState> {

    /**
     * Reduces the latest [MviViewState] with the [MviResult].
     */
    fun VS.reduce(result: R): VS
}