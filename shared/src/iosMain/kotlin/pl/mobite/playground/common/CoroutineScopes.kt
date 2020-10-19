package pl.mobite.playground.common

import kotlinx.coroutines.*

fun mainScope(): CoroutineScopeIOS = CoroutineScopeIOS(MainScope())