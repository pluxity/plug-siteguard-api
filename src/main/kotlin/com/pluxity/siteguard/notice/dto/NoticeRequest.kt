package com.pluxity.siteguard.notice.dto

import com.pluxity.siteguard.notice.entity.Notice
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "공지사항 등록/수정 요청")
data class NoticeRequest(
    @field:Schema(description = "제목", example = "공지사항 제목입니다", required = true)
    @field:NotBlank(message = "제목은 필수입니다")
    val title: String,
)

fun NoticeRequest.toEntity(): Notice = Notice(title = this.title)
