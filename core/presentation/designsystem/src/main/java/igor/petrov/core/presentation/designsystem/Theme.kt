package igor.petrov.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = runningAppGreen,
    background = runningAppBlack,
    surface = runningAppDarkGray,
    secondary = runningAppWhite,
    primaryContainer = runningAppGreen30,
    onPrimary = runningAppBlack,
    onBackground = runningAppWhite,
    onSurface = runningAppWhite,
    onSurfaceVariant = runningAppGray,
    onError = runningAppDarkRed,
    errorContainer = runningAppDarkRed5
)

@Composable
fun RunningAppTheme(
    // Dynamic color is available on Android 12+
     content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode){
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}