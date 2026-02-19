package com.pluxity.siteguard.systemsetting.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema(description = "시스템 설정 요청")
data class SystemSettingRequest(
    @field:Min(value = 1, message = "롤링 간격은 1초 이상이어야 합니다.")
    @field:Schema(description = "롤링 간격(초)", example = "10")
    val rollingIntervalSeconds: Int,
)
