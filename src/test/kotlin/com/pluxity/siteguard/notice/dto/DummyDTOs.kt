package com.pluxity.siteguard.notice.dto

fun dummyNoticeRequest(title: String = "테스트 공지사항") =
    NoticeRequest(
        title = title,
    )
