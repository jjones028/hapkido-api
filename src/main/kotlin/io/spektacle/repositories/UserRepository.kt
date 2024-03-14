package io.spektacle.repositories

import io.spektacle.models.User
import io.spektacle.singletons.DatabaseSingleton.dbQuery
import io.spektacle.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository() : Repository<User, Long> {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName]
    )

    override suspend fun findByIdOrNull(id: Long) = dbQuery {
        Users.selectAll()
            .where(Users.id eq id)
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun findAll() = dbQuery {
        val users = Users.selectAll().map(::resultRowToUser)
        users
    }

    override suspend fun update(model: User) = dbQuery {
        Users.update {
            it[id] = model.id
            it[username] = model.username
            it[firstName] = model.firstName
            it[lastName] = model.lastName
        } == 1
    }

    override suspend fun delete(id: Long) = dbQuery {
        Users.deleteWhere { Users.id eq id } == 1
    }

    override suspend fun insert(model: User) = dbQuery {
        val insert = Users.insert {
            it[id] = model.id
            it[username] = model.username
            it[firstName] = model.firstName
            it[lastName] = model.lastName
        }
        insert.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }
}