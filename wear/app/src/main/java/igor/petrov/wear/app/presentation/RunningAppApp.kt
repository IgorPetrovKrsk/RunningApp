package igor.petrov.wear.app.presentation

import android.app.Application
import igor.petrov.wear.app.presentation.di.runPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RunningAppApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RunningAppApp)
            modules(
                runPresentationModule
            )
        }
    }
}