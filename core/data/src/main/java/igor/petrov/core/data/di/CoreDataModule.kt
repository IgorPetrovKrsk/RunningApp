package igor.petrov.core.data.di

import igor.petrov.core.data.auth.EncryptedSessionStorage
import igor.petrov.core.data.networking.HttpClientFactory
import igor.petrov.core.data.run.OfflineFirstRunRepository
import igor.petrov.core.domain.run.RunRepository
import igor.petrov.core.domain.util.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()

}