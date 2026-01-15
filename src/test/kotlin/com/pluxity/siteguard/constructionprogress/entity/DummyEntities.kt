package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyConstructionProgress(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    phaseName: String = "터파기",
    plannedRate: Float = 100.0f,
    actualRate: Float = 100.0f,
    plannedProgressRate: Float = 5.0f,
    actualProgressRate: Float = 5.0f,
) = ConstructionProgress(
    workDate = workDate,
    phaseName = phaseName,
    plannedRate = plannedRate,
    actualRate = actualRate,
    plannedProgressRate = plannedProgressRate,
    actualProgressRate = actualProgressRate,
).withId(id)
