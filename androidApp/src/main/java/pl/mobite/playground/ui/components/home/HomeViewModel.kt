package pl.mobite.playground.ui.components.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import pl.mobite.playground.domain.home.HomeViewStateCache
import pl.mobite.playground.domain.home.mvi.HomeMviController

/**
 * View model is used only to provide proper scope to MviController.
 * MviController could be used without ViewModel with some custom scope.
 */
class HomeViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(), KoinComponent {

    val homeMviController: HomeMviController by inject { parametersOf(savedStateHandle, viewModelScope) }
}
