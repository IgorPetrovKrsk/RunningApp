package igor.petrov.wear.run.presentation

import igor.petrov.core.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
    data class Error(val message: UiText):TrackerEvent
}