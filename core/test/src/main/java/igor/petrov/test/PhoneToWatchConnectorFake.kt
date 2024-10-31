package igor.petrov.test

import igor.petrov.core.connectivity.domain.DeviceNode
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import igor.petrov.core.domain.util.Result
import igor.petrov.run.domain.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class PhoneToWatchConnectorFake : WatchConnector {

    private var sendError: MessagingError? = null

    private val _isTrackable = MutableStateFlow(true)

    private val _connectedDevice = MutableStateFlow<DeviceNode?>(null)

    override val connectedDevice: StateFlow<DeviceNode?>
        get() = _connectedDevice.asStateFlow()


    private val _messagingActions = MutableSharedFlow<MessagingAction>()

    override val messagingActions: Flow<MessagingAction>
        get() = _messagingActions.asSharedFlow()

    override fun setIsTrackable(isTrackable: Boolean) {

    }

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return if (sendError == null) {
            Result.Success(Unit)
        } else {
            Result.Error(sendError!!)
        }
    }

    suspend fun sendFromWatchToPhone(action: MessagingAction) {
        _messagingActions.emit(action)

    }
}