package com.pluxity.siteguard.user.dto

import com.pluxity.siteguard.permission.dto.PermissionResponse
import com.pluxity.siteguard.permission.dto.toPermissionResponse
import com.pluxity.siteguard.user.entity.Role

data class RoleResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val permissions: List<PermissionResponse>,
)

fun Role.toRoleResponse() =
    RoleResponse(
        this.requiredId,
        this.name,
        this.description,
        this.rolePermissions
            .map { it.permission }
            .sortedByDescending { it.id }
            .map { it.toPermissionResponse() }
            .toList(),
    )
