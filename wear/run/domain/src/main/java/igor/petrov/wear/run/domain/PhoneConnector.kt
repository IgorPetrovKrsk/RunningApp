package igor.petrov.wear.run.domain

import igor.petrov.core.connectivity.domain.DeviceNode
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PhoneConnector {
    val connectedNode: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError>
}