package igor.petrov.wear.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import igor.petrov.core.notification.ActiveRunService
import igor.petrov.core.presentation.designsystem_wear.RunningappTheme
import igor.petrov.wear.run.presentation.TrackerScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            RunningappTheme {
                TrackerScreenRoot(onServiceToggle = { shouldStartRunning ->
                    if (shouldStartRunning) {
                        startService(ActiveRunService.createStartIntent(applicationContext, this::class.java))
                    } else {
                        startService(ActiveRunService.createStopIntent(applicationContext))
                    }
                })
            }
        }
    }

}
