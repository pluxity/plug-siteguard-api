package com.pluxity.siteguard.constructionprogress.dto

import com.pluxity.siteguard.constructionprogress.entity.ConstructionProgress
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Schema(description = "공정현황 등록/수정 요청")
data class ConstructionProgressRequest(
    @field:Schema(description = "공정현황 ID (수정 시 필수, 등록 시 null)", example = "1")
    val id: Long?,
    @field:Schema(description = "작업일", example = "2026-01-15", required = true)
    @field:NotNull(message = "작업일은 필수입니다")
    var workDate: LocalDate,
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

fun ConstructionProgressRequest.toEntity(): ConstructionProgress =
    ConstructionProgress(
        workDate = this.workDate,
        phaseName = this.phaseName,
        plannedRate = this.plannedRate,
        actualRate = this.actualRate,
        plannedProgressRate = this.plannedProgressRate,
        actualProgressRate = this.actualProgressRate,
    )
