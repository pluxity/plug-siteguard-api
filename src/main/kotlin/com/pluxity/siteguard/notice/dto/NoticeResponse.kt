package com.pluxity.siteguard.notice.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.pluxity.siteguard.global.response.BaseResponse
import com.pluxity.siteguard.global.response.toBaseResponse
import com.pluxity.siteguard.notice.entity.Notice
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공지사항 응답")
data class NoticeResponse(
    @field:Schema(description = "ID", example = "1")
    val id: Long,
    @field:Schema(description = "제목", example = "공지사항 제목입니다")
    val title: String,
    @field:Schema(description = "내용", example = "공지사항 내용입니다")
    val content: String?,
    @field:JsonUnwrapped
    val baseResponse: BaseResponse,
)

fun Notice.toResponse(): NoticeResponse =
    NoticeResponse(
        id = this.requiredId,
        title = this.title,
        content = this.content,
        baseResponse = this.toBaseResponse(),
    )
