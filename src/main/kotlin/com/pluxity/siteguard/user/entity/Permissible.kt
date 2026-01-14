package com.pluxity.siteguard.user.entity

import com.pluxity.siteguard.permission.ResourceType

interface Permissible {
    val resourceId: String

    val resourceType: ResourceType
}
