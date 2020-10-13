package pl.mobite.playground.common

import kotlin.native.concurrent.freeze

fun <T>freeze(obj: T) {
    obj.freeze()
}
