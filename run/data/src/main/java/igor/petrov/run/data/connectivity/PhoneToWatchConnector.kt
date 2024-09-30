@file:Suppress("OPT_IN_USAGE")

package igor.petrov.run.data.connectivity

import igor.petrov.core.connectivity.domain.DeviceNode
import igor.petrov.core.connectivity.domain.DeviceType
import igor.petrov.core.connectivity.domain.NodeDiscovery
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingClient
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import igor.petrov.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class PhoneToWatchConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
):WatchConnector {
    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedDevice = _connectedNode.asStateFlow()

    private val isTrackable = MutableStateFlow(false)

    override val messagingActions: Flow<MessagingAction> = nodeDiscovery
        .observeConnectedDevices(DeviceType.PHONE)
        .flatMapLatest {connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if(node != null && node.isNearby) {
                _connectedNode.value = node
                messagingClient.connectToNote(node.id)
            } else flowOf()
        }.onEach { action ->
            if (action == MessagingAction.ConnectionRequest){
                if (isTrackable.value){
                    messagingClient.sendOrQueueAction(MessagingAction.Trackable)
                } else {
                    messagingClient.sendOrQueueAction(MessagingAction.Untrackable)
                }
            }
        }
        .shareIn(
            applicationScope,
            SharingStarted.Eagerly
        )

    init {
        _connectedNode
            .filterNotNull()
            .flatMapLatest { isTrackable }
            .onEach { isTrackable ->
                sendActionToWatch(MessagingAction.ConnectionRequest)
                if(isTrackable) {
                    sendActionToWatch(MessagingAction.Trackable)
                } else {
                    sendActionToWatch(MessagingAction.Untrackable)
                }
            }
            .launchIn(applicationScope)
    }

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        this.isTrackable.value = isTrackable
    }
}