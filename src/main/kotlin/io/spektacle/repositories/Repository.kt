package io.spektacle.repositories

import org.jetbrains.exposed.sql.ResultRow

interface Repository<T, ID> {

    fun toModel(row: ResultRow) : T

    suspend fun findByIdOrNull(id: ID) : T?

    suspend fun findAll() : Iterable<T>

    suspend fun create(model: T) : T?

    suspend fun update(model: T) : Boolean

    suspend fun delete(id: ID) : Boolean

}
