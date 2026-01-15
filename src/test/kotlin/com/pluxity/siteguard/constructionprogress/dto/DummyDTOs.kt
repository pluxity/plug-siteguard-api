package com.pluxity.siteguard.constructionprogress.dto

import com.pluxity.siteguard.constructionprogress.entity.PhaseName
import java.time.LocalDate

fun dummyConstructionProgressRequest(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    phaseName: PhaseName = PhaseName.EARTHWORK,
    plannedRate: Int = 100,
    actualRate: Int = 100,
) = ConstructionProgressRequest(
    id = id,
    workDate = workDate,
    phaseName = phaseName,
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
