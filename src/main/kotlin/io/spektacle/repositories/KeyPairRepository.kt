package io.spektacle.repositories

import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64
import io.spektacle.db.DatabaseSingleton.dbQuery
import io.spektacle.db.tables.KeyPairs
import io.spektacle.models.ExistingKeyPair
import io.spektacle.models.KeyPair
import io.spektacle.models.NewKeyPair
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class KeyPairRepository : Repository<ExistingKeyPair, NewKeyPair, Long> {
    override fun toModel(row: ResultRow) = ExistingKeyPair(
        id = row[KeyPairs.id],
        privateKey = row[KeyPairs.privateKey].decodeBase64String(),
        publicKey = row[KeyPairs.publicKey].decodeBase64String()
    )

    override suspend fun findAll() = dbQuery {
        KeyPairs.selectAll().map(::toModel)
    }

    override suspend fun delete(id: Long) = dbQuery {
        KeyPairs.deleteWhere { KeyPairs.id eq id } == 1
    }

    override suspend fun update(model: KeyPair<Long>) = dbQuery {
        KeyPairs.update {
            it[id] = model.id
            it[privateKey] = model.privateKey.encodeBase64()
            it[publicKey] = model.publicKey.encodeBase64()
        } == 1
    }

    override suspend fun create(model: NewKeyPair) = dbQuery {
        val insert = KeyPairs.insert {
            it[privateKey] = model.privateKey.encodeBase64()
            it[publicKey] = model.publicKey.encodeBase64()
        }
        insert.resultedValues?.singleOrNull()?.let(::toModel)
    }

    override suspend fun findByIdOrNull(id: Long) = dbQuery {
        KeyPairs.selectAll()
            .where(KeyPairs.id eq id)
            .map(::toModel)
            .singleOrNull()
    }
}
