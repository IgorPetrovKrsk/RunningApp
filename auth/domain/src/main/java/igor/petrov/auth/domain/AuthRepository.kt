package igor.petrov.auth.domain

import igor.petrov.core.domain.util.DataError
import igor.petrov.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email: String, password: String):EmptyResult<DataError.Network>
}