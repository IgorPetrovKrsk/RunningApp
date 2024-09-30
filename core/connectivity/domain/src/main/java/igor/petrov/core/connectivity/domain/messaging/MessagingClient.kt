package igor.petrov.core.connectivity.domain.messaging

import igor.petrov.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface MessagingClient {
    fun connectToNote(nodeId: String): Flow<MessagingAction>
    suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError>
}