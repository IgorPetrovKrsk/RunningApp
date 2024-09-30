package igor.petrov.wear.app.presentation

import android.app.Application
import igor.petrov.core.connectivity.data.di.coreConnectivityDataModule
import igor.petrov.wear.app.presentation.di.appModule
import igor.petrov.wear.app.presentation.di.wearRunPresentationModule
import igor.petrov.wear.run.data.di.wearRunDataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RunningAppApp:Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RunningAppApp)
            modules(
                appModule,
                wearRunDataModule,
                wearRunPresentationModule,
                coreConnectivityDataModule
            )
        }
    }
}