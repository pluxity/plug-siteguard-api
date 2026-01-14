package com.pluxity.siteguard.global.annotation

import com.pluxity.siteguard.permission.PermissionLevel
import com.pluxity.siteguard.permission.ResourceType
import com.pluxity.siteguard.user.entity.PermissionAction

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckPermission(
    val action: PermissionAction = PermissionAction.READ_SINGLE,
    val resourceType: ResourceType = ResourceType.NONE,
    val level: PermissionLevel = PermissionLevel.READ,
    val idParamIndex: Int = 0,
)
