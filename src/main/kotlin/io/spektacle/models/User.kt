package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class User<ID: Long?>(
    val id: ID,
    val username: String,
    val firstName: String,
    val lastName: String? = null
)
typealias ExistingUser = User<Long>
typealias NewUser = User<Nothing?>