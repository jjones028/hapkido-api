package io.spektacle.services

import io.spektacle.models.KeyPair
import io.spektacle.process.ProcessExecutor
import io.spektacle.repositories.KeyPairRepository
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText


class OpenSSLKeyPairService(private val repository: KeyPairRepository) : KeyPairService {
    override suspend fun generate(): KeyPair {

        val (privateKey, privateKeyFilename) = generatePrivateKey()
        val (publicKey, publicKeyFilename) = generatePublicKey(privateKeyFilename)

        Path(privateKeyFilename).deleteIfExists()
        Path(publicKeyFilename).deleteIfExists()

        return KeyPair(privateKey, publicKey)
    }

    override suspend fun create(keyPair: KeyPair) =
        repository.create(keyPair)

    override suspend fun delete(id: Long) =
        repository.delete(id)

    override suspend fun update(keyPair: KeyPair) =
        repository.update(keyPair)

    override suspend fun findByIdOrNull(id: Long) =
        repository.findByIdOrNull(id)

    override suspend fun findAll() =
        repository.findAll()

    private suspend fun generatePrivateKey(): Pair<String, String> {
        val privateKeyTempFile = createTempFile(UUID.randomUUID().toString())
        ProcessExecutor(
            listOf(
                "openssl",
                "genpkey",
                "-out",
                privateKeyTempFile.absolutePathString(),
                "-algorithm",
                "RSA",
                "-pkeyopt",
                "rsa_keygen_bits:2048",
                "-aes-128-cbc",
                "-quiet",
                "-pass",
                "pass:"
            )
        ).executeCommand()

        return Pair(privateKeyTempFile.readText(), privateKeyTempFile.absolutePathString())
    }

    private suspend fun generatePublicKey(privateKeyFilename: String): Pair<String, String> {
        val publicKeyTempFile = createTempFile(UUID.randomUUID().toString())

        ProcessExecutor(
            listOf(
                "openssl",
                "rsa",
                "-in",
                privateKeyFilename,
                "-pubout",
                "-out",
                publicKeyTempFile.absolutePathString(),
                "-passin",
                "pass:"
            )
        ).executeCommand()

        return Pair(publicKeyTempFile.readText(), publicKeyTempFile.absolutePathString())
    }
}
