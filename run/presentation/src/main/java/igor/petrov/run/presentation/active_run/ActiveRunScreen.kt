@file:OptIn(ExperimentalMaterial3Api::class)

package igor.petrov.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import igor.petrov.core.notification.ActiveRunService
import igor.petrov.core.presentation.designsystem.RunningAppTheme
import igor.petrov.core.presentation.designsystem.StartIcon
import igor.petrov.core.presentation.designsystem.StopIcon
import igor.petrov.core.presentation.designsystem.components.RunningAppActionButton
import igor.petrov.core.presentation.designsystem.components.RunningAppDialog
import igor.petrov.core.presentation.designsystem.components.RunningAppFloatingActionButton
import igor.petrov.core.presentation.designsystem.components.RunningAppOutlinedActionButton
import igor.petrov.core.presentation.designsystem.components.RunningAppScaffold
import igor.petrov.core.presentation.designsystem.components.RunningAppToolbar
import igor.petrov.core.presentation.ui.ObserveAsEvents
import igor.petrov.run.presentation.R
import igor.petrov.run.presentation.active_run.components.RunDataCard
import igor.petrov.run.presentation.active_run.maps.TrackerMap
import igor.petrov.run.presentation.util.hasLocationPermission
import igor.petrov.run.presentation.util.hasNotificationPermission
import igor.petrov.run.presentation.util.shouldShowLocationPermissionRationale
import igor.petrov.run.presentation.util.shouldShowNotificationRationale
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream

@Composable

fun ActiveRunScreenRoot(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is ActiveRunEvent.Error -> {
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }

            ActiveRunEvent.RunSaved -> onFinish()
        }

    }
    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = { action ->
            when (action) {
                is ActiveRunAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning) {
                        onBack()
                    }

                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasCourseLocationPermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRational = activity.shouldShowNotificationRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationRational
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRational = activity.shouldShowLocationPermissionRationale()
        val showNotification = activity.shouldShowNotificationRationale()
        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = activity.shouldShowLocationPermissionRationale()
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = activity.shouldShowNotificationRationale()
            )
        )

        if (!showLocationRational && !showNotification) {
            permissionLauncher.requestRunningAppPermissions(context)
        }
    }

    val isServiceActive by ActiveRunService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.shouldTrack) {
        if (context.hasLocationPermission() && state.shouldTrack && !isServiceActive) {
            onServiceToggle(true)
        }
    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if (state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    RunningAppScaffold(
        withGradient = false,
        topAppBar = {
            RunningAppToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                },
            )
        },
        floatingActionButton = {
            RunningAppFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                iconSize = 20.dp,
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcessed(stream.toByteArray()))
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )

        }
    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        RunningAppDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = {
                onAction(ActiveRunAction.OnResumeRunClick)
            },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RunningAppActionButton(
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            },
            secondaryButton = {
                RunningAppOutlinedActionButton(
                    text = stringResource(id = R.string.finish),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }



    if (state.showLocationRationale || state.showNotificationRationale) {
        RunningAppDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = { /*Normal dismissing not allowed for permissions */ },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                RunningAppOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRunningAppPermissions(context)
                    }
                )
            })
    }

}

private fun ActivityResultLauncher<Array<String>>.requestRunningAppPermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else arrayOf()
    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }

        !hasLocationPermission -> launch(locationPermissions)
        !hasNotificationPermission -> launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RunningAppTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onServiceToggle = {},
            onAction = {}
        )
    }
}