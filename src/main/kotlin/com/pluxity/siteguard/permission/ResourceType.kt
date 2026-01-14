package com.pluxity.siteguard.permission

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException

enum class ResourceType(
    val resourceName: String,
    val endpoint: String,
) {
    NONE("NONE", ""),
    USER("사용자관리", "users"),
    ATTENDANCE_STATUS("출역현황", "attendance-status"),
    PROCESS_STATUS("공정현황", "process-status"),
    MANAGEMENT_NOTE("주요관리사항", "management-note"),
    TARGET_MANAGEMENT("목표 관리", "target-management"),
    ;

    companion object {
        fun fromString(resourceName: String): ResourceType =
            entries.firstOrNull { it != NONE && it.name.equals(resourceName, ignoreCase = true) }
                ?: throw CustomException(ErrorCode.INVALID_RESOURCE_TYPE, resourceName)
    }
}
