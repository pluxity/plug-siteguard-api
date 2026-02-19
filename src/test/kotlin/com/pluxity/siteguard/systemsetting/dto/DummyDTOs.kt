package com.pluxity.siteguard.systemsetting.dto

fun dummySystemSettingRequest(rollingIntervalSeconds: Int = 10) =
    SystemSettingRequest(
        rollingIntervalSeconds = rollingIntervalSeconds,
    )
