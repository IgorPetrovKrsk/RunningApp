package igor.petrov.run.presentation.di

import igor.petrov.run.domain.RunningTracker
import igor.petrov.run.presentation.active_run.ActiveRunViewModel
import igor.petrov.run.presentation.run_overview.RunOverviewViewmodel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::ActiveRunViewModel)
    viewModelOf(::RunOverviewViewmodel)
}