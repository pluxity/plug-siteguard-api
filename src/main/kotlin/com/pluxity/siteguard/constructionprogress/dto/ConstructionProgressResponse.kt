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
    @field:Schema(description = "공정명")
    val workType: WorkTypeResponse,
    @field:Schema(description = "목표율", example = "80")
    val plannedRate: Int,
    @field:Schema(description = "공정률", example = "75")
    val actualRate: Int,
)

fun ConstructionProgress.toResponse(): ConstructionProgressResponse =
    ConstructionProgressResponse(
        id = this.requiredId,
        workDate = this.workDate,
        workType = this.workType.toResponse(),
        plannedRate = this.plannedRate,
        actualRate = this.actualRate,
    )
