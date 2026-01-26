package com.pluxity.siteguard.notice.dto

fun dummyNoticeRequest(
    title: String = "테스트 공지사항",
    content: String = "테스트 공지사항 내용입니다",
) = NoticeRequest(
    title = title,
    content = content,
)
