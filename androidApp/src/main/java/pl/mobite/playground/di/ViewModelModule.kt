package pl.mobite.playground.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mobite.playground.ui.components.home.HomeViewModel

val viewModelModule = module {

    viewModel { (savedStateHandle: SavedStateHandle) ->
        HomeViewModel(savedStateHandle = savedStateHandle)
    }
}