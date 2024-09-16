package igor.petrov.wear.run.presentation

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
}