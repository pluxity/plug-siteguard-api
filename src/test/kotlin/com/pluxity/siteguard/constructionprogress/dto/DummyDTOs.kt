package com.pluxity.siteguard.constructionprogress.dto

import java.time.LocalDate

fun dummyConstructionProgressRequest(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    phaseName: String = "터파기",
    plannedRate: Float = 100.0f,
    actualRate: Float = 100.0f,
    plannedProgressRate: Float = 5.0f,
    actualProgressRate: Float = 5.0f,
) = ConstructionProgressRequest(
    id = id,
    workDate = workDate,
    phaseName = phaseName,
    plannedRate = plannedRate,
    actualRate = actualRate,
    plannedProgressRate = plannedProgressRate,
    actualProgressRate = actualProgressRate,
)

fun dummyConstructionProgressBulkRequest(
    upserts: List<ConstructionProgressRequest> = emptyList(),
    deletedIds: List<Long> = emptyList(),
) = ConstructionProgressBulkRequest(
    upserts = upserts,
    deletedIds = deletedIds,
)

fun dummyConstructionProgressSearch(
    page: Int = 1,
    size: Int = 10,
) = ConstructionProgressSearch(
    page = page,
    size = size,
)
