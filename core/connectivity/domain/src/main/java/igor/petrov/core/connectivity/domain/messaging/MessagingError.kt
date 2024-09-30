package igor.petrov.core.connectivity.domain.messaging

import igor.petrov.core.domain.util.Error

enum class MessagingError: Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}