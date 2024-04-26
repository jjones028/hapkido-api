package io.spektacle.models

import kotlinx.serialization.Serializable

@Serializable
data class SubjectName(
    val id: Long? = null,
    val commonName: String,
    val countryCode: String,
    val organization: String,
    val stateOrProvince: String? = null,
    val locality: String? = null,
    val organizationalUnit: String? = null,
    val emailAddress: String? = null
)
