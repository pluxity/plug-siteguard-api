package com.pluxity.siteguard.constructionprogress.dto

import java.time.LocalDate

fun dummyWorkTypeRequest(name: String = "토공") =
    WorkTypeRequest(
        name = name,
    )

fun dummyConstructionProgressRequest(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    workTypeId: Long = 1L,
    plannedRate: Int = 100,
    actualRate: Int = 100,
) = ConstructionProgressRequest(
    id = id,
    workDate = workDate,
    workTypeId = workTypeId,
    plannedRate = plannedRate,
    actualRate = actualRate,
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
