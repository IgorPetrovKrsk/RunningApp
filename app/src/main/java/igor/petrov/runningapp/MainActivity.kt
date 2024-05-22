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
import igor.petrov.core.presentation.designsystem.AnalyticsIcon
import igor.petrov.core.presentation.designsystem.RunningAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunningAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Icon(imageVector = AnalyticsIcon, contentDescription = null, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
