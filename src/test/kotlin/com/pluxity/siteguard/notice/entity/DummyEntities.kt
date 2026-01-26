package com.pluxity.siteguard.notice.entity

import com.pluxity.siteguard.base.entity.withAudit
import com.pluxity.siteguard.base.entity.withId

fun dummyNotice(
    id: Long? = null,
    title: String = "테스트 공지사항",
    content: String = "테스트 공지사항 내용입니다",
) = Notice(
    title = title,
    content = content,
).withId(id).withAudit()
