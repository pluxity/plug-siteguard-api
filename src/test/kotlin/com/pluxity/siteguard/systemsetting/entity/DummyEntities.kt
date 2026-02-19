package com.pluxity.siteguard.systemsetting.entity

import com.pluxity.siteguard.base.entity.withAudit

fun dummySystemSetting(rollingIntervalSeconds: Int = 10) =
    SystemSetting(
        rollingIntervalSeconds = rollingIntervalSeconds,
    ).withAudit()
