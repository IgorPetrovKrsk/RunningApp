package igor.petrov.auth.data

import igor.petrov.auth.domain.AuthRepository
import igor.petrov.core.data.networking.post
import igor.petrov.core.domain.util.DataError
import igor.petrov.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
):AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}