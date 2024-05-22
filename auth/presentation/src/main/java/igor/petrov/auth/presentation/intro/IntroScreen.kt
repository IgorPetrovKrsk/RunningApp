
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import igor.petrov.auth.presentation.R
import igor.petrov.auth.presentation.intro.IntroAction
import igor.petrov.core.presentation.designsystem.LogoIcon
import igor.petrov.core.presentation.designsystem.RunningAppTheme
import igor.petrov.core.presentation.designsystem.components.GradientBackground
import igor.petrov.core.presentation.designsystem.components.RunningAppActionButton
import igor.petrov.core.presentation.designsystem.components.RunningAppOutlinedActionButton

@Composable
fun IntroScreenRoot(
    onSingUpClick: () -> Unit,
    onSingInClick: () -> Unit
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnSighUpClick -> onSingUpClick()
                IntroAction.OnSingInClick -> onSingInClick()
            }
        }
    )
}

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {
    GradientBackground() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RunningAppLogoVertical()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(text = stringResource(id = R.string.welcome_to_running_app),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.running_app_description),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            RunningAppOutlinedActionButton(
                text = stringResource(id = R.string.sing_in),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSingInClick)
                },
                modifier = Modifier
                    .fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            RunningAppActionButton(
                text = stringResource(id = R.string.sing_up),
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(IntroAction.OnSighUpClick)
                })
        }
    }

}

@Composable
private fun RunningAppLogoVertical(modifier: Modifier = Modifier) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = LogoIcon, contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(id = R.string.running_app),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview
@Composable
private fun IntroScreenPreview() {
    RunningAppTheme {
        IntroScreen(
            onAction = {}
        )
    }

}