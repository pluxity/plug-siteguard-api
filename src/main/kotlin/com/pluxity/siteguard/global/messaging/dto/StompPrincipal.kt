package com.pluxity.siteguard.global.messaging.dto

import com.pluxity.siteguard.global.utils.UUIDUtils
import java.security.Principal

class StompPrincipal(
    private val name: String = UUIDUtils.generateUUID(),
) : Principal {
    override fun getName(): String = name
}
