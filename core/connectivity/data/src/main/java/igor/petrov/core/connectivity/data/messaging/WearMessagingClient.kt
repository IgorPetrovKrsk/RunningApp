package igor.petrov.core.connectivity.data.messaging

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import igor.petrov.core.connectivity.domain.messaging.MessagingAction
import igor.petrov.core.connectivity.domain.messaging.MessagingClient
import igor.petrov.core.connectivity.domain.messaging.MessagingError
import igor.petrov.core.domain.util.EmptyResult
import igor.petrov.core.domain.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WearMessagingClient(
    context: Context
): MessagingClient {

    private val client = Wearable.getMessageClient(context)
    private val messageQueue = mutableListOf<MessagingAction>()
    private var connectedNodeId: String? = null

    override fun connectToNote(nodeId: String): Flow<MessagingAction> {
        connectedNodeId = nodeId

        return callbackFlow {
            val listener: (MessageEvent) -> Unit = {event ->
                if(event.path.startsWith(BASE_PATH_MESSAGING_ACTION)){
                    val json = event.data.decodeToString()
                    val action = Json.decodeFromString<MessagingActionDto>(json)
                    trySend(action.toMessagingAction())
                }
            }
            client.addListener (listener)
            messageQueue.forEach{
                sendOrQueueAction(it)
            }
            messageQueue.clear()
            awaitClose {
                client.removeListener(listener)
            }
        }


    }

    override suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError> {
        return connectedNodeId?.let{ id ->
            try{
                val json = Json.encodeToString(action.toMessagingActionDto())
                client.sendMessage(id, BASE_PATH_MESSAGING_ACTION,json.toByteArray()).await()
                Result.Success(Unit)
            } catch (e: ApiException){
                Result.Error(
                    if (e.status.isInterrupted){
                        MessagingError.CONNECTION_INTERRUPTED
                    } else MessagingError.UNKNOWN
                )
            }
        }?: run {
            messageQueue.add(action)
            Result.Error(MessagingError.DISCONNECTED)
        }
    }

    companion object {
        private const val BASE_PATH_MESSAGING_ACTION = "runningapp/messaging_action"
    }
}