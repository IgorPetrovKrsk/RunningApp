package igor.petrov.runningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import igor.petrov.core.presentation.designsystem.AnalyticsIcon
import igor.petrov.core.presentation.designsystem.RunningAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }

        enableEdgeToEdge()
        setContent {
            RunningAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Icon(imageVector = AnalyticsIcon, contentDescription = null, Modifier.padding(innerPadding))
                    if (!viewModel.state.isCheckingAuth) {
                        val navController = rememberNavController()
                        NavigationRoot(navController = navController,
                            isLoggedIn = viewModel.state.isLoggedIn)
                    }
                }
            }
        }
    }
}
