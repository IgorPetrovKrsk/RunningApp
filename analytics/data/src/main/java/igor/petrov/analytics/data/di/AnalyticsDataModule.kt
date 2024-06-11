package igor.petrov.analytics.data.di

import igor.petrov.analytics.data.RoomAnalyticsRepository
import igor.petrov.analytics.domain.AnalyticsRepository
import igor.petrov.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single {
        get<RunDatabase>().analyticsDao
    }
}