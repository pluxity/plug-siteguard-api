package com.pluxity.siteguard.permission.dto

import com.pluxity.siteguard.permission.PermissionLevel
import com.pluxity.siteguard.permission.ResourceType
import io.swagger.v3.oas.annotations.media.Schema

data class PermissionRequest(
    @field:Schema(
        description = "자원 유형",
        implementation = ResourceType::class,
        example = "FACILITY",
    ) val resourceType: String,
    @field:Schema(
        description = "자원 아이디",
    ) val resourceIds: List<String> = listOf(),
    @field:Schema(
        description = "권한 레벨",
        implementation = PermissionLevel::class,
        example = "READ",
    ) val level: PermissionLevel = PermissionLevel.READ,
)
