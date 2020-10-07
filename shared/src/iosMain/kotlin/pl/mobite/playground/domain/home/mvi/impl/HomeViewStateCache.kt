package pl.mobite.playground.domain.home.mvi.impl

import pl.mobite.playground.common.mvi.api.MviViewStateCache
import pl.mobite.playground.common.mvi.impl.MviViewStateCacheIOS

actual class HomeViewStateCache : MviViewStateCache<HomeViewState> by MviViewStateCacheIOS()