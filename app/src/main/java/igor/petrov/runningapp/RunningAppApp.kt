package igor.petrov.runningapp

import android.app.Application
import igor.petrov.auth.data.di.authDataModule
import igor.petrov.auth.presentation.di.authViewModelModule
import igor.petrov.core.data.di.coreDataModule
import igor.petrov.runningapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RunningAppApp:Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RunningAppApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule
            )
        }
    }

}