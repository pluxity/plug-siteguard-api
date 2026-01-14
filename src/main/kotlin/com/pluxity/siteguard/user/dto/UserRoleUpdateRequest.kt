package com.pluxity.siteguard.user.dto

data class UserRoleUpdateRequest(
    val roleIds: List<Long> = emptyList(),
)
