package com.pluxity.siteguard.targetmanagement.dto

import com.pluxity.siteguard.targetmanagement.entity.ConstructionSection
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시공구간 응답")
data class ConstructionSectionResponse(
    @field:Schema(description = "시공구간 코드", example = "CUTTING")
    val code: String,
    @field:Schema(description = "시공구간 설명", example = "절토")
    val description: String,
)

fun ConstructionSection.toResponse(): ConstructionSectionResponse =
    ConstructionSectionResponse(
        code = this.name,
        description = this.description,
    )
