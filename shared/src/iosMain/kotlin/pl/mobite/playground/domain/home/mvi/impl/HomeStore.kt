package pl.mobite.playground.domain.home.mvi.impl

import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.common.MviStore
import pl.mobite.playground.domain.home.mvi.HomeMviController

class HomeStore : MviStore<HomeAction, HomeResult, HomeViewState>() {
    override val mviController: HomeMviController by inject { parametersOf(storeScope) }
}