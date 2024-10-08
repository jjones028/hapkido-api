package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class KeyPair<ID: Long?>(
    val id: ID,
    val privateKey: String,
    val publicKey: String
)
typealias ExistingKeyPair = KeyPair<Long>
typealias NewKeyPair = KeyPair<Nothing?>
