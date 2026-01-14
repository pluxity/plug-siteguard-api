package com.pluxity.siteguard.permission

import com.pluxity.siteguard.permission.dto.ResourceItemResponse

interface ResourceDataProvider {
    val resourceType: ResourceType

    fun findAllResources(): List<ResourceItemResponse>
}
