package com.pluxity.siteguard.constructionprogress.dto

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
    @field:Schema(description = "공정명 ID", example = "1", required = true)
    @field:NotNull(message = "공정명 ID는 필수입니다")
    var workTypeId: Long,
    @field:Schema(description = "목표율", example = "80")
    val plannedRate: Int,
    @field:Schema(description = "공정률", example = "75")
    val actualRate: Int,
)
