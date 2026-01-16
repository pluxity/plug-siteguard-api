package com.pluxity.siteguard.permission

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException

enum class ResourceType(
    val resourceName: String,
    val endpoint: String,
) {
    NONE("NONE", ""),
    USER("사용자관리", "users"),
    ATTENDANCE_STATUS("출역현황", "attendance"),
    CONSTRUCTION_PROGRESS("공정현황", "process-statuses"),
    MANAGEMENT_NOTE("주요관리사항", "key-management"),
    TARGET_MANAGEMENT("목표관리", "goals"),
    ;

    companion object {
        fun fromString(resourceName: String): ResourceType =
            entries.firstOrNull { it != NONE && it.name.equals(resourceName, ignoreCase = true) }
                ?: throw CustomException(ErrorCode.INVALID_RESOURCE_TYPE, resourceName)
    }
}
