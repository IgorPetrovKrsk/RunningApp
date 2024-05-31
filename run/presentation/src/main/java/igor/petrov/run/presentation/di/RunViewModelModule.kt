package igor.petrov.run.presentation.di

import igor.petrov.run.presentation.runOverview.RunOverviewViewmodel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewmodel)
}