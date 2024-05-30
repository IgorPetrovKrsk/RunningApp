package igor.petrov.core.data.auth

import igor.petrov.core.domain.util.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable{
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo{
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}