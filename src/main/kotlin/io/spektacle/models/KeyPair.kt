package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class KeyPair(
    val id: Long? = null,
    val privateKey: String,
    val publicKey: String
) {
    constructor(privateKey: String, publicKey: String) : this(
        null,
        privateKey,
        publicKey
    )
}
