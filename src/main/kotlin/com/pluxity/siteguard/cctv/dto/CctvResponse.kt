package com.pluxity.siteguard.cctv.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.pluxity.siteguard.cctv.entity.Cctv
import com.pluxity.siteguard.global.response.BaseResponse
import com.pluxity.siteguard.global.response.toBaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "CCTV 응답")
data class CctvResponse(
    @field:Schema(description = "ID", example = "1")
    val id: Long,
    @field:Schema(description = "경로", example = "cam1")
    val path: String,
    @field:Schema(description = "이름", example = "1번 카메라")
    val name: String?,
    @field:Schema(description = "경도", example = "127.0")
    val lon: Double?,
    @field:Schema(description = "위도", example = "37.0")
    val lat: Double?,
    @field:Schema(description = "즐겨찾기 여부", example = "false")
    val isFavorite: Boolean,
    @field:JsonUnwrapped
    val baseResponse: BaseResponse,
)

fun Cctv.toResponse(): CctvResponse =
    CctvResponse(
        id = this.requiredId,
        path = this.path,
        name = this.name,
        lon = this.lon,
        lat = this.lat,
        isFavorite = this.isFavorite,
        baseResponse = this.toBaseResponse(),
    )
