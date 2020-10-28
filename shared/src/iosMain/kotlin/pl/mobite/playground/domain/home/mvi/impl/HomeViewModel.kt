package pl.mobite.playground.domain.home.mvi.impl

import kotlinx.coroutines.MainScope
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.common.ViewModel
import pl.mobite.playground.domain.home.mvi.HomeMviController

class HomeViewModel: ViewModel<HomeAction, HomeResult, HomeViewState>(MainScope()) {
    override val mviController: HomeMviController by inject { parametersOf(coroutineScope) }
}