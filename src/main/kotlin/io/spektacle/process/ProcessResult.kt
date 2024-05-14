package io.spektacle.process

data class ProcessResult(
    val exitCode: Int,
    val message: String,
    val errorMessage: String
)
