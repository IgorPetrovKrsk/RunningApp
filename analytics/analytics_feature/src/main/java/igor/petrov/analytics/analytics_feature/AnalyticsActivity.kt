package igor.petrov.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import igor.petrov.analytics.data.di.analyticsModule
import igor.petrov.analytics.presentation.AnalyticsDashboardScreenRoot
import igor.petrov.analytics.presentation.di.analyticsPresentationModule
import igor.petrov.core.presentation.designsystem.RunningAppTheme
import org.koin.core.context.loadKoinModules

class AnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
            listOf(
                analyticsModule,
                analyticsPresentationModule
            )
        )
        SplitCompat.installActivity(this)

        setContent {
            RunningAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "analytics_dashboard"
                ) {
                    composable("analytics_dashboard") {
                        AnalyticsDashboardScreenRoot(onBackClick = {
                            finish()
                        }
                        )
                    }
                }
            }
        }
    }
}