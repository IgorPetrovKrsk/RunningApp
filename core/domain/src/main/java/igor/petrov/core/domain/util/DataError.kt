package igor.petrov.core.domain.util

sealed interface DataError:Error {
    enum class Network:DataError{
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUEST,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIAZIATION,
        UNKNOWN
    }
    enum class Local:DataError{
        DISK_FULL,

    }
}