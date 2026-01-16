package com.pluxity.siteguard.constructionprogress.controller

import com.pluxity.siteguard.constructionprogress.dto.ProcessStatusBulkRequest
import com.pluxity.siteguard.constructionprogress.dto.ProcessStatusResponse
import com.pluxity.siteguard.constructionprogress.dto.ProcessStatusSearch
import com.pluxity.siteguard.constructionprogress.service.ProcessStatusService
import com.pluxity.siteguard.global.response.DataResponseBody
import com.pluxity.siteguard.global.response.ErrorResponseBody
import com.pluxity.siteguard.global.response.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/process-statuses")
@Tag(name = "Process Status Controller", description = "공정현황 관리 API")
class ProcessStatusController(
    private val service: ProcessStatusService,
) {
    @Operation(summary = "공정현황 전체 조회", description = "공정현황 전체 목록을 조회합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponseBody::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping
    fun findAll(
        @Parameter(description = "조회 페이지번호", example = "1")
        @RequestParam("page") page: Int = 1,
        @Parameter(description = "페이지당 개수", example = "9999")
        @RequestParam("size") size: Int = 9999,
    ): ResponseEntity<DataResponseBody<PageResponse<ProcessStatusResponse>>> =
        ResponseEntity.ok(DataResponseBody(service.findAll(ProcessStatusSearch(page, size))))

    @Operation(summary = "공정현황 저장/수정/삭제", description = "공정현황을 저장, 수정, 삭제합니다. upserts의 id가 없으면 생성, 있으면 수정합니다. deletedIds에 포함된 id는 삭제됩니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "송출 성공"),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponseBody::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponseBody::class),
                    ),
                ],
            ),
        ],
    )
    @PutMapping
    fun saveOrUpdateAll(
        @RequestBody @Valid request: ProcessStatusBulkRequest,
    ): ResponseEntity<Void> {
        service.saveOrUpdateAll(request)
        return ResponseEntity.noContent().build()
    }
}
