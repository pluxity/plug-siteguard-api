package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyConstructionProgress(
    id: Long? = null,
    workDate: LocalDate = LocalDate.of(2026, 1, 15),
    phaseName: PhaseName = PhaseName.EARTHWORK,
    plannedRate: Int = 100,
    actualRate: Int = 100,
) = ConstructionProgress(
    workDate = workDate,
    phaseName = phaseName,
    plannedRate = plannedRate,
    actualRate = actualRate,
).withId(id)
