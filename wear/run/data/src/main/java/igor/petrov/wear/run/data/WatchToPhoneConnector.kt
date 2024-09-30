package igor.petrov.wear.run.data

import igor.petrov.core.connectivity.domain.DeviceNode
import igor.petrov.core.connectivity.domain.DeviceType
import igor.petrov.core.connectivity.domain.NodeDiscovery
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingClient
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import igor.petrov.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalCoroutinesApi::class)
class WatchToPhoneConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
): PhoneConnector {
    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedNode = _connectedNode.asStateFlow()

    override val messagingActions = nodeDiscovery
        .observeConnectedDevices(DeviceType.WATCH)
        .flatMapLatest {connectedNodes ->
            val node = connectedNodes.firstOrNull()
            if (node != null && node.isNearby){
                _connectedNode.value = node
                messagingClient.connectToNote(node.id)
            } else flowOf()
        }.shareIn(
            applicationScope,
            SharingStarted.Eagerly)

    override suspend fun sendActionToPhone(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }
}