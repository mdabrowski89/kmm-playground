package pl.mobite.playground.common.mvi

import pl.mobite.playground.common.Parcelable
import pl.mobite.playground.common.Parcelize
import pl.mobite.playground.common.RawValue
import pl.mobite.playground.common.randomUUID

interface MviEvent<T> {
    val value: T
    val id: String
}

// TODO: think of some better division
@Parcelize
data class MviEventParcelable<T: Parcelable>(
    override val value: T,
    override val id: String = randomUUID()
) : MviEvent<T>, Parcelable

@Parcelize
data class MviEventRaw<T>(
    override val value: @RawValue T,
    override val id: String = randomUUID()
) : MviEvent<T>, Parcelable
