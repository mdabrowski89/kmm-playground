package pl.mobite.playground.common.mvi.api

import pl.mobite.playground.common.Parcelable

/**
 * Representation of an UI state
 */
interface MviViewState : Parcelable {
    fun isSavable(): Boolean
}