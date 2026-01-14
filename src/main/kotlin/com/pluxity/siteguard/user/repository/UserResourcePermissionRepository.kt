package com.pluxity.siteguard.user.repository

import com.pluxity.siteguard.permission.ResourceType
import com.pluxity.siteguard.user.entity.UserResourcePermission
import org.springframework.data.jpa.repository.JpaRepository

interface UserResourcePermissionRepository : JpaRepository<UserResourcePermission, Long> {
    fun existsByUserIdAndResourceTypeAndResourceId(
        userId: Long,
        resourceType: ResourceType,
        resourceId: String,
    ): Boolean

    fun deleteByResourceTypeAndResourceId(
        resourceType: ResourceType,
        resourceId: String,
    )
}
