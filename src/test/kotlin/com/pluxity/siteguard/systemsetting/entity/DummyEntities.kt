package com.pluxity.siteguard.systemsetting.entity

import com.pluxity.siteguard.base.entity.withAudit

fun dummySystemSetting(
    rollingIntervalSeconds: Int = 10,
    bimThumbnailFileId: Long? = null,
    aerialViewFileId: Long? = null,
) = SystemSetting(
    rollingIntervalSeconds = rollingIntervalSeconds,
    bimThumbnailFileId = bimThumbnailFileId,
    aerialViewFileId = aerialViewFileId,
).withAudit()
