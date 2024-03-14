package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String? = null
)
