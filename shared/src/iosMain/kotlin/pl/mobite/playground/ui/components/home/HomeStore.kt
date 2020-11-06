package pl.mobite.playground.ui.components.home

import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.ui.base.MviStore
import pl.mobite.playground.domain.home.mvi.HomeMviController
import pl.mobite.playground.domain.home.mvi.impl.HomeAction
import pl.mobite.playground.domain.home.mvi.impl.HomeResult
import pl.mobite.playground.domain.home.mvi.impl.HomeViewState

class HomeStore : MviStore<HomeAction, HomeResult, HomeViewState>() {

    override val initialState = HomeViewState()

    override val mviController: HomeMviController by inject { parametersOf(initialState, storeScope) }
}