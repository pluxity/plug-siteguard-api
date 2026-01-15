package com.pluxity.siteguard.targetmanagement.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "목표관리 일괄 저장/수정/삭제 요청")
data class TargetManagementBulkRequest(
    @field:Schema(description = "저장/수정할 목표관리 목록")
    val upserts: List<TargetManagementRequest> = emptyList(),
    @field:Schema(description = "삭제할 목표관리 ID 목록", example = "[1, 2]")
    val deletedIds: List<Long> = emptyList(),
)
