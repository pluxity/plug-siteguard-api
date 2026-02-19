package com.pluxity.siteguard.systemsetting.dto

import com.pluxity.siteguard.systemsetting.entity.SystemSetting
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "시스템 설정 응답")
data class SystemSettingResponse(
    @field:Schema(description = "롤링 간격(초)")
    val rollingIntervalSeconds: Int? = null,
)

fun SystemSetting.toResponse(): SystemSettingResponse =
    SystemSettingResponse(
        rollingIntervalSeconds = rollingIntervalSeconds,
    )
