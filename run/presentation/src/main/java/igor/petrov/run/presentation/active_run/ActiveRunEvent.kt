package igor.petrov.run.presentation.active_run

import igor.petrov.core.presentation.ui.UiText

sealed interface ActiveRunEvent {
    data class Error(val error: UiText): ActiveRunEvent
    data object RunSaved:ActiveRunEvent
}