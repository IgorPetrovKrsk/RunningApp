package igor.petrov.wear.app.presentation.di

import igor.petrov.wear.app.presentation.RunningAppApp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        (androidApplication() as RunningAppApp).applicationScope
    }
}