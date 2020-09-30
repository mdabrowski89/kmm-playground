package pl.mobite.playground.model

import pl.mobite.playground.common.Parcelable
import pl.mobite.playground.common.Parcelize

@Parcelize
data class Task(
    val id: Long,
    val content: String,
    val isDone: Boolean
) : Parcelable