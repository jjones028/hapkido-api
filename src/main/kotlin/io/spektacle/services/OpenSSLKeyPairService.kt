package io.spektacle.services

import io.spektacle.models.KeyPair
import java.util.concurrent.TimeUnit


class OpenSSLKeyPairService : KeyPairService {
    override suspend fun generate(): KeyPair {
        val privateKey = runCatching {
            ProcessBuilder(
                "openssl",
                "genpkey",
                "-out",
                "-",
                "-algorithm",
                "RSA",
                "-pkeyopt",
                "rsa_keygen_bits:2048",
                "-aes-128-cbc",
                "-quiet",
                "-pass",
                "pass:"
            ).redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectErrorStream(true)
                .start().also {
                    it.waitFor(60L, TimeUnit.MINUTES)
                    if (it.isAlive) it.destroy()
                }.inputStream.bufferedReader().readText()
        }
            .onFailure { it.printStackTrace() }.getOrNull()

        val publicKey = runCatching {
//            openssl rsa -in privkey.pem -pubout -out key.pub
            ProcessBuilder(
                "openssl",
                "rsa",
//                "-in",
//                "-",
                "-pubout",
                "-out",
                "-",
                "-passin",
                "pass:"
            ).redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectErrorStream(true)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .start().also {
                    it.waitFor(60L, TimeUnit.SECONDS)
                    if (it.isAlive) it.destroy()
                }
                .also { it.outputStream.bufferedWriter().write(privateKey!!) }
                .inputStream.bufferedReader().readText()
        }
            .onFailure { it.printStackTrace() }.getOrNull()
//        println(result)
        return KeyPair(privateKey ?: "", publicKey ?: "")
    }
}
