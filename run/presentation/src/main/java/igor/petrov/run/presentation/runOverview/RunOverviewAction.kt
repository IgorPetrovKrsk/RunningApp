package igor.petrov.run.presentation.runOverview

sealed interface RunOverviewAction {
    data object OnStartRunClick : RunOverviewAction
    data object OnLogOutClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
}