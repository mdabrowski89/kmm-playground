package pl.mobite.playground.common.mvi

import pl.mobite.playground.common.Parcelable
import pl.mobite.playground.common.Parcelize
import pl.mobite.playground.common.RawValue
import pl.mobite.playground.common.randomUUID

abstract class MviEvent<T> : Parcelable {
    abstract val value: T
    abstract val id: String

    /**
     * Static creator methods are ued in order to hide implementation details of each MviEvent type
     */
    companion object {

        fun create(value: Parcelable): MviEvent<Parcelable> = MviEventParcelable(value)

        fun create(value: Boolean): MviEvent<Boolean> = MviEventRaw(value)
        fun create(value: Int): MviEvent<Int> = MviEventRaw(value)
        fun create(value: Long): MviEvent<Long> = MviEventRaw(value)
        fun create(value: String): MviEvent<String> = MviEventRaw(value)
        fun create(value: Throwable): MviEvent<Throwable> = MviEventRaw(value)
    }
}

@Parcelize
private data class MviEventParcelable<T: Parcelable>(
    override val value: T,
    override val id: String = randomUUID()
)  : MviEvent<T>()

@Parcelize
private data class MviEventRaw<T>(
    override val value: @RawValue T,
    override val id: String = randomUUID()
) : MviEvent<T>()

