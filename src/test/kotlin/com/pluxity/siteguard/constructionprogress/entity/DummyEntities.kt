package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyWorkType(
    id: Long? = null,
    name: String = "토공",
) = WorkType(
    name = name,
).withId(id)

fun dummyConstructionProgress(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    workType: WorkType = dummyWorkType(id = 1L),
    plannedRate: Int = 100,
    actualRate: Int = 100,
) = ConstructionProgress(
    workDate = workDate,
    workType = workType,
    plannedRate = plannedRate,
    actualRate = actualRate,
).withId(id)
