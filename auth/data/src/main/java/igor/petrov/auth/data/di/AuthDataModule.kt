package igor.petrov.auth.data.di

import igor.petrov.auth.data.AuthRepositoryImpl
import igor.petrov.auth.data.EmailPatternValidator
import igor.petrov.auth.domain.AuthRepository
import igor.petrov.auth.domain.PatternValidator
import igor.petrov.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}

