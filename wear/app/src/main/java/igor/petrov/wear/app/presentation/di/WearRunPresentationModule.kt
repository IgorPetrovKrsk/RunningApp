package igor.petrov.wear.app.presentation.di

import igor.petrov.wear.run.data.HealthServicesExerciseTracker
import igor.petrov.wear.run.domain.ExerciseTracker
import igor.petrov.wear.run.presentation.TrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunPresentationModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
    viewModelOf(::TrackerViewModel)
}