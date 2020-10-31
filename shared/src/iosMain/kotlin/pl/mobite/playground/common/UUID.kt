package pl.mobite.playground.common

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()