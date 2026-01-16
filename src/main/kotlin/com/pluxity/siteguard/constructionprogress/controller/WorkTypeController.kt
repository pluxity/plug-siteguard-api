package com.pluxity.siteguard.constructionprogress.controller

import com.pluxity.siteguard.constructionprogress.dto.WorkTypeRequest
import com.pluxity.siteguard.constructionprogress.dto.WorkTypeResponse
import com.pluxity.siteguard.constructionprogress.service.WorkTypeService
import com.pluxity.siteguard.global.annotation.ResponseCreated
import com.pluxity.siteguard.global.response.DataResponseBody
import com.pluxity.siteguard.global.response.ErrorResponseBody
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/work-types")
@Tag(name = "Work Type Controller", description = "공정명 관리 API")
class WorkTypeController(
    private val service: WorkTypeService,
) {
    @Operation(summary = "공정명 전체 조회", description = "공정명 전체 목록을 조회합니다")
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
    fun findAll(): ResponseEntity<DataResponseBody<List<WorkTypeResponse>>> = ResponseEntity.ok(DataResponseBody(service.findAll()))

    @Operation(summary = "공정명 등록", description = "공정명을 등록합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "등록 성공"),
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
        ],
    )
    @PostMapping
    @ResponseCreated(path = "/work-types/{id}")
    fun create(
        @RequestBody @Valid request: WorkTypeRequest,
    ): ResponseEntity<Long> = ResponseEntity.ok(service.create(request))

    @Operation(summary = "공정명 삭제", description = "공정명을 삭제합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "삭제 성공"),
            ApiResponse(
                responseCode = "404",
                description = "공정명을 찾을 수 없음",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponseBody::class),
                    ),
                ],
            ),
        ],
    )
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}
