package io.spektacle.repositories

interface Repository<T, ID> {
    suspend fun findByIdOrNull(id: ID) : T?

    suspend fun findAll() : Iterable<T>

    suspend fun insert(model: T) : T?

    suspend fun update(model: T) : Boolean

    suspend fun delete(id: ID) : Boolean

}