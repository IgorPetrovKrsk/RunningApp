package igor.petrov.android_test

import igor.petrov.core.domain.util.AuthInfo
import igor.petrov.core.domain.util.SessionStorage

class SessionStorageFake: SessionStorage {
    private var authInfo: AuthInfo? = null

    override suspend fun get(): AuthInfo? {
        return authInfo
    }

    override suspend fun set(info: AuthInfo?) {
        authInfo = info
    }
}