package io.spektacle.repositories

import io.spektacle.db.DatabaseSingleton.dbQuery
import io.spektacle.db.tables.Users
import io.spektacle.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserRepository : Repository<User, Long> {
    override fun toModel(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName]
    )

    override suspend fun findByIdOrNull(id: Long) = dbQuery {
        Users.selectAll()
            .where(Users.id eq id)
            .map(::toModel)
            .singleOrNull()
    }

    override suspend fun findAll() = dbQuery {
        Users.selectAll().map(::toModel)
    }

    override suspend fun update(model: User) = dbQuery {
        model.id?.let {
            Users.update {
            it[id] = model.id
            it[username] = model.username
            it[firstName] = model.firstName
            it[lastName] = model.lastName
            } == 1
        } ?: false
    }

    override suspend fun delete(id: Long) = dbQuery {
        Users.deleteWhere { Users.id eq id } == 1
    }

    override suspend fun insert(model: User) = dbQuery {
        val insert = Users.insert {
            it[username] = model.username
            it[firstName] = model.firstName
            it[lastName] = model.lastName
        }
        insert.resultedValues?.singleOrNull()?.let(::toModel)
    }
}
