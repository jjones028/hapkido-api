package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class KeyPair(
    val privateKey: String,
    val publicKey: String
)
