package com.pluxity.siteguard.file.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.pluxity.siteguard.file.entity.FileEntity
import com.pluxity.siteguard.file.entity.ZipContentEntry
import com.pluxity.siteguard.global.response.BaseResponse
import com.pluxity.siteguard.global.response.toBaseResponse
import io.swagger.v3.oas.annotations.media.Schema

data class FileResponse(
    @field:Schema(description = "파일 ID", example = "1")
    var id: Long? = null,
    @field:Schema(description = "파일 URL")
    var url: String? = null,
    @field:Schema(description = "원본 파일명")
    var originalFileName: String? = null,
    @field:Schema(description = "Content Type")
    var contentType: String? = null,
    @field:Schema(description = "파일 상태")
    var fileStatus: String? = null,
    @field:Schema(description = "ZIP 파일 루트 내용 (ZIP 파일인 경우에만)")
    var zipContents: List<ZipEntryInfo> = emptyList(),
    @field:JsonUnwrapped var baseResponse: BaseResponse? = null,
)

fun FileEntity.toFileResponse(
    url: String?,
    zipContentEntries: List<ZipContentEntry> = emptyList(),
) = FileResponse(
    id = this.requiredId,
    url = url ?: this.filePath,
    originalFileName = this.originalFileName,
    contentType = this.contentType,
    fileStatus = this.fileStatus.toString(),
    zipContents = zipContentEntries.map { it.toZipEntryInfo() },
    baseResponse = this.toBaseResponse(),
)
