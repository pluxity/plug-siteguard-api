package com.pluxity.siteguard.processstatus.dto

import java.time.LocalDate

fun dummyWorkTypeRequest(name: String = "토공") =
    WorkTypeRequest(
        name = name,
    )

fun dummyProcessStatusRequest(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    workTypeId: Long = 1L,
    plannedRate: Int = 100,
    actualRate: Int = 100,
) = ProcessStatusRequest(
    id = id,
    workDate = workDate,
    workTypeId = workTypeId,
    plannedRate = plannedRate,
    actualRate = actualRate,
)

fun dummyProcessStatusBulkRequest(
    upserts: List<ProcessStatusRequest> = emptyList(),
    deletedIds: List<Long> = emptyList(),
) = ProcessStatusBulkRequest(
    upserts = upserts,
    deletedIds = deletedIds,
)

fun dummyProcessStatusSearch(
    page: Int = 1,
    size: Int = 10,
) = ProcessStatusSearch(
    page = page,
    size = size,
)
