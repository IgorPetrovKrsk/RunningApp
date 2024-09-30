package igor.petrov.run.domain

import igor.petrov.core.connectivity.domain.DeviceNode
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevice: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    fun setIsTrackable(isTrackable: Boolean)

    suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError>
}