package io.spektacle.services

import io.spektacle.models.KeyPair

interface KeyPairService {
    suspend fun generate(): KeyPair

    suspend fun create(keyPair: KeyPair): KeyPair?

    suspend fun delete(id: Long): Boolean

    suspend fun update(keyPair: KeyPair): Boolean

    suspend fun findByIdOrNull(id: Long): KeyPair?

    suspend fun findAll(): List<KeyPair>
}
