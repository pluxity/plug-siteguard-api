package com.pluxity.siteguard.file.strategy.storage

data class FilePersistenceContext(
    val filePath: String,
    val newPath: String,
    val contentType: String,
    val originalFileName: String,
)
