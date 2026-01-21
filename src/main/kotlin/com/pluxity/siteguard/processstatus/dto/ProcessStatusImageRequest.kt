package com.pluxity.siteguard.processstatus.dto

import com.pluxity.siteguard.processstatus.entity.ProcessStatusImage
import io.swagger.v3.oas.annotations.media.Schema
import org.jetbrains.annotations.NotNull

@Schema(description = "공정관련 이미지 수정 요청")
data class ProcessStatusImageRequest(
    @field:Schema(description = "파일 ID", example = "1")
    @field:NotNull
    val fileId: Long,
)

fun ProcessStatusImageRequest.toEntity(): ProcessStatusImage = ProcessStatusImage(fileId = this.fileId)
