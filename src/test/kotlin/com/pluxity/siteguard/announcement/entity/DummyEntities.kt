package com.pluxity.siteguard.announcement.entity

import com.pluxity.siteguard.base.entity.withAudit

fun dummyAnnouncement(
    id: Long = Announcement.SINGLETON_ID,
    content: String = "테스트 안내사항",
) = Announcement(
    id = id,
    content = content,
).withAudit()
