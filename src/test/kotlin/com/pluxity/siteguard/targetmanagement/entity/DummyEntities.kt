package com.pluxity.siteguard.targetmanagement.entity

import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyTargetManagement(
    id: Long? = null,
    inputDate: LocalDate = LocalDate.of(2026, 1, 15),
    constructionSection: ConstructionSection = ConstructionSection.CUTTING,
    progressRate: Float = 75.5f,
    constructionRate: Float = 80.0f,
    totalQuantity: Int = 1000,
    cumulativeQuantity: Int = 755,
    previousCumulativeQuantity: Int = 700,
    targetQuantity: Int = 800,
    workQuantity: Int = 50,
    startDate: LocalDate = LocalDate.of(2026, 1, 1),
    completionDate: LocalDate = LocalDate.of(2026, 12, 31),
    plannedWorkDays: Int = 365,
    delayDays: Int = 0,
) = TargetManagement(
    inputDate = inputDate,
    constructionSection = constructionSection,
    progressRate = progressRate,
    constructionRate = constructionRate,
    totalQuantity = totalQuantity,
    cumulativeQuantity = cumulativeQuantity,
    previousCumulativeQuantity = previousCumulativeQuantity,
    targetQuantity = targetQuantity,
    workQuantity = workQuantity,
    startDate = startDate,
    completionDate = completionDate,
    plannedWorkDays = plannedWorkDays,
    delayDays = delayDays,
).withId(id)
