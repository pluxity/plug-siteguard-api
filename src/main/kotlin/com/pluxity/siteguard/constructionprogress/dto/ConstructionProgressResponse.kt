package com.pluxity.siteguard.constructionprogress.dto

import com.pluxity.siteguard.constructionprogress.entity.ConstructionProgress
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "공정현황 응답")
data class ConstructionProgressResponse(
    @field:Schema(description = "공정현황 ID", example = "1")
    val id: Long,
    @field:Schema(description = "작업일", example = "2026-01-15")
    val workDate: LocalDate,
    @field:Schema(description = "공정명", example = "거푸집설치")
    val phaseName: String,
    @field:Schema(description = "목표율", example = "80.0")
    val plannedRate: Float,
    @field:Schema(description = "공정률", example = "75.0")
    val actualRate: Float,
    @field:Schema(description = "목표공정률", example = "35.0")
    val plannedProgressRate: Float,
    @field:Schema(description = "현재공정률", example = "30.0")
    val actualProgressRate: Float,
)

fun ConstructionProgress.toResponse(): ConstructionProgressResponse =
    ConstructionProgressResponse(
        id = this.requiredId,
        workDate = this.workDate,
        phaseName = this.phaseName,
        plannedRate = this.plannedRate,
        actualRate = this.actualRate,
        plannedProgressRate = this.plannedProgressRate,
        actualProgressRate = this.actualProgressRate,
    )
