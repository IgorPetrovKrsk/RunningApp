package igor.petrov.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick: AnalyticsAction
}