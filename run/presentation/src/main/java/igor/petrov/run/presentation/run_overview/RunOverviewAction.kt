package igor.petrov.run.presentation.run_overview

import igor.petrov.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartRunClick : RunOverviewAction
    data object OnLogOutClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
    data class DeleteRun(val runUi: RunUi): RunOverviewAction
}