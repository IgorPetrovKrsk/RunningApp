@file:OptIn(ExperimentalMaterial3Api::class)

package igor.petrov.run.presentation.runOverview

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import igor.petrov.core.presentation.designsystem.AnalyticsIcon
import igor.petrov.core.presentation.designsystem.LogoIcon
import igor.petrov.core.presentation.designsystem.LogoutIcon
import igor.petrov.core.presentation.designsystem.RunIcon
import igor.petrov.core.presentation.designsystem.RunningAppTheme
import igor.petrov.core.presentation.designsystem.components.RunningAppFloatingActionButton
import igor.petrov.core.presentation.designsystem.components.RunningAppScaffold
import igor.petrov.core.presentation.designsystem.components.RunningAppToolbar
import igor.petrov.core.presentation.designsystem.components.util.DropDownItem
import igor.petrov.run.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable

fun RunOverviewScreenRoot(
    onStartRunClick: () -> Unit,
    viewModel: RunOverviewViewmodel = koinViewModel()
) {
    RunOverviewScreen(
        //state = viewModel.state,
        onAction = {action ->
            when (action){
                RunOverviewAction.OnStartRunClick -> onStartRunClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RunOverviewScreen(
    //state: ,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    RunningAppScaffold(
        topAppBar = {
            RunningAppToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.running_app),
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logout)
                    )

                ),
                onMenuItemClick = {index ->
                    when (index){
                         0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                         1 -> onAction(RunOverviewAction.OnLogOutClick)
                    }
                },
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            RunningAppFloatingActionButton(
                icon = RunIcon,
                onClick = {
                    onAction(RunOverviewAction.OnStartRunClick)
                }
            )
        }
    ) {padding ->


    }

}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RunningAppTheme {
        RunOverviewScreen(
            //state = (),
            onAction = {}
        )
    }
}