package com.pluxity.siteguard.constructionprogress.dto

import com.pluxity.siteguard.constructionprogress.entity.PhaseName
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공정명 응답")
data class PhaseNameResponse(
    @field:Schema(description = "공정명 코드", example = "EARTHWORK")
    val code: String,
    @field:Schema(description = "공정명 설명", example = "토공")
    val description: String,
)

fun PhaseName.toResponse(): PhaseNameResponse =
    PhaseNameResponse(
        code = this.name,
        description = this.description,
    )
