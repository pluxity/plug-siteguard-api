package com.pluxity.siteguard.global.utils

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException

fun <T> findAllByIdsOrThrow(
    ids: List<Long>,
    findAllById: (List<Long>) -> List<T>,
    idExtractor: (T) -> Long,
    errorCode: ErrorCode,
): Map<Long, T> {
    if (ids.isEmpty()) return emptyMap()

    val entities = findAllById(ids)
    val foundIds = entities.map(idExtractor).toSet()
    val notFoundIds = ids.filter { it !in foundIds }
    if (notFoundIds.isNotEmpty()) {
        throw CustomException(errorCode, notFoundIds)
    }
    return entities.associateBy(idExtractor)
}
