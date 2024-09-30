package igor.petrov.runningapp

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import igor.petrov.auth.data.di.authDataModule
import igor.petrov.auth.presentation.di.authViewModelModule
import igor.petrov.core.connectivity.data.di.coreConnectivityDataModule
import igor.petrov.core.data.di.coreDataModule
import igor.petrov.core.database.di.databaseModule
import igor.petrov.run.data.di.runDataModule
import igor.petrov.run.location.di.locationModule
import igor.petrov.run.network.di.networkModule
import igor.petrov.run.presentation.di.runPresentationModule
import igor.petrov.runningapp.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RunningAppApp:Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RunningAppApp)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule,
                runDataModule,
                coreConnectivityDataModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

}