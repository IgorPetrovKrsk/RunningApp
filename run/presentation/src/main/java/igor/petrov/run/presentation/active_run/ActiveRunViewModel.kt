package igor.petrov.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.domain.location.Location
import igor.petrov.core.domain.run.Run
import igor.petrov.core.domain.run.RunRepository
import igor.petrov.core.domain.util.Result
import igor.petrov.core.notification.ActiveRunService
import igor.petrov.core.presentation.ui.asUiText
import igor.petrov.run.domain.LocationDataCalculator
import igor.petrov.run.domain.RunningTracker
import igor.petrov.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.roundToInt

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val runRepository: RunRepository,
    private val watchConnector: WatchConnector,
    private val applicationScope: CoroutineScope
) : ViewModel() {

    var state by mutableStateOf(ActiveRunState(
        shouldTrack = ActiveRunService.isServiceActive.value && runningTracker.isTracking.value,
        hasStartedRunning = ActiveRunService.isServiceActive.value
    ))
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(false)

    private val isTracking = combine(
        shouldTrack, hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        watchConnector
            .connectedDevice
            .filterNotNull()
            .onEach {
                Timber.d("New device detected: ${it.displayName}")
            }
            .launchIn(viewModelScope)

        hasLocationPermission.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObservingLocation()
            } else {
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)
        }.launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach { locationWithAltitude ->
                state = state.copy(currentLocation = locationWithAltitude?.location)
            }.launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach { runData ->
                state = state.copy(runData = runData)
            }.launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach { duration ->
                state = state.copy(elapsedTime = duration)
            }.launchIn(viewModelScope)

        listenToWatchActions()
    }

    fun onAction(action: ActiveRunAction, triggeredOnWatch: Boolean = false) {
        if (!triggeredOnWatch){
            val messagingAction = when (action) {
                ActiveRunAction.OnFinishRunClick -> MessagingAction.Finish
                ActiveRunAction.OnResumeRunClick -> MessagingAction.StartOrResume
                ActiveRunAction.OnToggleRunClick -> {
                    if (state.hasStartedRunning){
                        MessagingAction.Pause
                    } else {
                        MessagingAction.StartOrResume
                    }
                }
                else -> null
            }
            messagingAction?.let {
                viewModelScope.launch {
                    watchConnector.sendActionToWatch(it)
                }
            }

        }
        when (action) {
            ActiveRunAction.OnFinishRunClick -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }

            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }

            ActiveRunAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }

            ActiveRunAction.OnToggleRunClick -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }

            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationPermissionRationale
                )
            }

            is ActiveRunAction.SubmitNotificationInfo -> {
                state = state.copy(
                    showNotificationRationale = action.showNotificationPermissionRationale
                )

            }

            is ActiveRunAction.DismissRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }

            is ActiveRunAction.OnRunProcessed -> {
                finishRun(action.mapPictureBytes)
            }

            else -> {}
        }

    }

    private fun finishRun(mapPictureBytes: ByteArray) {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }

        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(locations),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(locations),
                mapPictureUrl = null,
                avgHeartRate = if (state.runData.heartRates.isEmpty()) {
                    null
                } else {
                    state.runData.heartRates.average().roundToInt()
                },
                maxHeartRate = if (state.runData.heartRates.isEmpty()) {
                    null
                } else {
                    state.runData.heartRates.max()
                }
            )

            runningTracker.finishRun()

            when (val result = runRepository.upsertRun(run, mapPictureBytes)) {
                is Result.Error -> {
                    eventChannel.send(ActiveRunEvent.Error(result.error.asUiText()))
                }

                is Result.Success -> {
                    eventChannel.send(ActiveRunEvent.RunSaved)
                }
            }

            state = state.copy(isSavingRun = true)
        }
    }

    private fun listenToWatchActions() {
        watchConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    MessagingAction.ConnectionRequest -> {
                        if (isTracking.value) {
                            watchConnector.sendActionToWatch(MessagingAction.StartOrResume)
                        }
                    }

                    MessagingAction.Finish -> {
                        onAction(
                            action = ActiveRunAction.OnFinishRunClick,
                            triggeredOnWatch = true
                        )
                    }

                    MessagingAction.Pause -> {
                        if (isTracking.value) {
                            onAction(
                                action = ActiveRunAction.OnToggleRunClick,
                                triggeredOnWatch = true
                            )
                        }
                    }

                    MessagingAction.StartOrResume -> {
                        if (!isTracking.value) {
                            if (state.hasStartedRunning) {
                                onAction(
                                    action = ActiveRunAction.OnResumeRunClick,
                                    triggeredOnWatch = true
                                )
                            } else {
                                onAction(
                                    action = ActiveRunAction.OnToggleRunClick,
                                    triggeredOnWatch = true
                                )
                            }

                        }
                    }

                    else -> Unit
                }

            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isServiceActive.value) {
            applicationScope.launch {
                watchConnector.sendActionToWatch(MessagingAction.Untrackable)
            }
            runningTracker.stopObservingLocation()
        }
    }
}