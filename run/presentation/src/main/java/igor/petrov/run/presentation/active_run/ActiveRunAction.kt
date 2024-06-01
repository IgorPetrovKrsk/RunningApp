package igor.petrov.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClick : ActiveRunAction
    data object OnFinishRunClick : ActiveRunAction
    data object OnResumeRunClick : ActiveRunAction
    data object OnBackClick : ActiveRunAction
    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationPermissionRationale: Boolean
    ): ActiveRunAction
    data class SubmitNotificationInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ): ActiveRunAction
    data object DismissRationaleDialog:ActiveRunAction

}