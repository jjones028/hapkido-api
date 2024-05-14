package io.spektacle.process

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.InputStream

class ProcessExecutor(private val command: List<String>) {

    suspend fun executeCommand(): ProcessResult = withContext(
        Dispatchers.IO
    ) {
        runCatching {
            val process = ProcessBuilder(command).start()
            val outputStream = async {
                readStream(process.inputStream)
            }
            val errorStream = async {
                readStream(process.errorStream)
            }
            val exitCode = process.waitFor()
            ProcessResult(
                exitCode = exitCode,
                message = outputStream.await(),
                errorMessage = errorStream.await()
            )
        }.onFailure {
            ProcessResult(
                exitCode = -1,
                message = "",
                errorMessage = it.localizedMessage
            )
        }.getOrThrow()
    }

    private fun readStream(inputStream: InputStream) =
        inputStream.bufferedReader().use { reader -> reader.readText() }
}
