package io.spektacle.services

import io.spektacle.models.KeyPair
import io.spektacle.process.ProcessExecutor
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText


class OpenSSLKeyPairService : KeyPairService {
    override suspend fun generate(): KeyPair {

        val (privateKey, privateKeyFilename) = generatePrivateKey()
        val (publicKey, publicKeyFilename) = generatePublicKey(privateKeyFilename)

        Path(privateKeyFilename).deleteIfExists()
        Path(publicKeyFilename).deleteIfExists()

        return KeyPair(privateKey, publicKey)
    }

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
