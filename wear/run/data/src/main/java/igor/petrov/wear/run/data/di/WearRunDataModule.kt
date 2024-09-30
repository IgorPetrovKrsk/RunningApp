package igor.petrov.wear.run.data.di

import igor.petrov.wear.run.data.HealthServicesExerciseTracker
import igor.petrov.wear.run.data.WatchToPhoneConnector
import igor.petrov.wear.run.domain.ExerciseTracker
import igor.petrov.wear.run.domain.PhoneConnector
import igor.petrov.wear.run.domain.RunningTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
    singleOf(::WatchToPhoneConnector).bind<PhoneConnector>()
    singleOf(::RunningTracker)
}