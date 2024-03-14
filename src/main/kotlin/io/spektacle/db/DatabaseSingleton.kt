package io.spektacle.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import io.spektacle.db.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.*

object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString() +
                (config.propertyOrNull("storage.dbFilePath")?.getString()?.let {
                    File(it).canonicalFile.absolutePath
                } ?: "")
        val username = config.propertyOrNull("storage.username")?.getString()
        val password = config.propertyOrNull("storage.password")?.getString()
        val database = Database.connect(createHikariDataSource(url = jdbcURL, driver = driverClassName, username = username, password = password))
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        username: String?,
        password: String?
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 3
        this.username = username
        this.password = password
//        isAutoCommit = false
//        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}