package igor.petrov.auth.presentation.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import igor.petrov.android_test.SessionStorageFake
import igor.petrov.android_test.TestMockEngine
import igor.petrov.android_test.loginResponseStub
import igor.petrov.auth.data.AuthRepositoryImpl
import igor.petrov.auth.data.EmailPatternValidator
import igor.petrov.auth.data.LoginRequest
import igor.petrov.auth.domain.UserDataValidator
import igor.petrov.core.data.networking.HttpClientFactory
import igor.petrov.test.MainCoroutineExtension
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class LoginViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var repositoryImpl: AuthRepositoryImpl
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var mockEngine: TestMockEngine

    @BeforeEach
    fun setUp(){
        sessionStorageFake = SessionStorageFake()

        val mockEngineConfig = MockEngineConfig().apply {
            requestHandlers.add{request ->
                val relativeUrl = request.url.encodedPath
                if (relativeUrl == "/login"){
                    respond (
                        content = ByteReadChannel(
                            text = Json.encodeToString(loginResponseStub)
                        ),
                        headers = headers {
                            set("Content-type", "application/json")
                        }
                    )
                } else {
                    respond(
                        content = byteArrayOf(),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }

        mockEngine = TestMockEngine(
            dispatcher = mainCoroutineExtension.testDispatcher,
            mockEngineConfig = mockEngineConfig
        )

        val httpClient = HttpClientFactory(
            sessionStorage = sessionStorageFake
        ).build(mockEngine)

        repositoryImpl = AuthRepositoryImpl(
            httpClient = httpClient,
            sessionStorage = sessionStorageFake
        )

        viewModel = LoginViewModel(
            authRepository = repositoryImpl,
            userDataValidator = UserDataValidator(
                patternValidator = EmailPatternValidator
            )
        )


    }

    @Test
    fun testLogin() = runTest{
        assertThat(viewModel.state.canLogin).isFalse()

        viewModel.state.email.edit {
            append("test@test.com")
        }
        viewModel.state.password.edit {
            append("Test12345")
        }

        viewModel.onAction(LoginAction.OnLoginClick)

        assertThat(viewModel.state.isLoggingIn).isFalse()
        assertThat(viewModel.state.email.text.toString()).isEqualTo("test@test.com")
        assertThat(viewModel.state.password.text.toString()).isEqualTo("Test12345")

        val loginRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/login"
        }
        assertThat(loginRequest).isNotNull()
        assertThat(loginRequest!!.headers.contains("x-api-key")).isTrue()

        val loginBody = Json.decodeFromString<LoginRequest>(loginRequest.body.toByteArray().decodeToString())

        assertThat(loginBody.email).isEqualTo("test@test.com")
        assertThat(loginBody.password).isEqualTo("Test12345")

        val session = sessionStorageFake.get()
        assertThat(session?.userId).isEqualTo(loginResponseStub.userId)
        assertThat(session?.accessToken).isEqualTo(loginResponseStub.accessToken)
        assertThat(session?.refreshToken).isEqualTo(loginResponseStub.refreshToken)
    }
}