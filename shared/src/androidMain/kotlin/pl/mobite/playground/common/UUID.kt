package pl.mobite.playground.common

import java.util.UUID

actual fun randomUUID() = UUID.randomUUID().toString()