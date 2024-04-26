package io.spektacle.services

import io.spektacle.models.KeyPair

interface KeyPairService {
    suspend fun generate(): KeyPair
}
