package pl.mobite.playground.common

import kotlin.native.concurrent.freeze

fun <T>freeze(obj: T) {
    if (obj is List<*>) {
        obj.forEach { it.freeze() }
    }
    obj.freeze()
}