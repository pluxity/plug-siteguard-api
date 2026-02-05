package com.pluxity.siteguard.file.extensions

import com.pluxity.siteguard.file.dto.FileResponse
import com.pluxity.siteguard.file.service.FileService

fun <T> FileService.getFileMapById(
    items: List<T>,
    idExtractor: (T) -> Long?,
): Map<Long, FileResponse> = getFileMapFromIds(items.mapNotNull(idExtractor))

fun <T> FileService.getFileMapByIds(
    items: List<T>,
    idExtractor: (T) -> List<Long?>,
): Map<Long, FileResponse> = getFileMapFromIds(items.flatMap(idExtractor).filterNotNull())

private fun FileService.getFileMapFromIds(fileIds: List<Long>): Map<Long, FileResponse> {
    if (fileIds.isEmpty()) return emptyMap()

    return getFiles(fileIds)
        .mapNotNull { response ->
            response.id?.let { it to response }
        }.toMap()
}
