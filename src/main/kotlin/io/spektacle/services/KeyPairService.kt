package io.spektacle.services

import io.spektacle.models.ExistingKeyPair
import io.spektacle.models.NewKeyPair

interface KeyPairService {
    suspend fun generate(): NewKeyPair

    suspend fun create(keyPair: NewKeyPair): ExistingKeyPair?

    suspend fun delete(id: Long): Boolean

    suspend fun update(keyPair: ExistingKeyPair): Boolean

    suspend fun findByIdOrNull(id: Long): ExistingKeyPair?

    suspend fun findAll(): List<ExistingKeyPair>
}
