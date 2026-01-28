package com.pluxity.siteguard.notice.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "공지사항 등록/수정 요청")
data class NoticeRequest(
    @field:Schema(description = "제목 (최대 255자)", example = "공지사항 제목입니다", required = true, maxLength = 255)
    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(max = 255, message = "제목은 최대 255자까지 입력 가능합니다")
    val title: String,
    @field:Schema(description = "내용 (최대 1000자)", example = "공지사항 내용입니다", required = true, maxLength = 1000)
    @field:NotBlank(message = "내용은 필수입니다")
    @field:Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다")
    val content: String,
)
