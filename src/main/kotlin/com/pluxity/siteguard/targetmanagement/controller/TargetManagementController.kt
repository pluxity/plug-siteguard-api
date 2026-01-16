package com.pluxity.siteguard.targetmanagement.controller

import com.pluxity.siteguard.global.response.DataResponseBody
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementBulkRequest
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementResponse
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementSearch
import com.pluxity.siteguard.targetmanagement.service.TargetManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
@RequestMapping("/target-managements")
@Tag(name = "Target Management Controller", description = "목표관리 API")
class TargetManagementController(
    private val service: TargetManagementService,
) {
    @Operation(summary = "목표관리 전체 조회", description = "목표관리 전체 목록을 조회합니다")
    @GetMapping
    fun findAll(
        @Parameter(description = "조회 페이지번호", example = "1")
        @RequestParam("page") page: Int = 1,
        @Parameter(description = "페이지당 개수", example = "9999")
        @RequestParam("size") size: Int = 9999,
    ): ResponseEntity<DataResponseBody<PageResponse<TargetManagementResponse>>> =
        ResponseEntity.ok(DataResponseBody(service.findAll(TargetManagementSearch(page, size))))

    @Operation(summary = "목표관리 저장/수정/삭제", description = "목표관리를 저장, 수정, 삭제합니다. upserts의 id가 없으면 생성, 있으면 수정합니다. deletedIds에 포함된 id는 삭제됩니다")
    @PutMapping
    fun saveOrUpdateAll(
        @RequestBody @Valid request: TargetManagementBulkRequest,
    ): ResponseEntity<Void> {
        service.saveOrUpdateAll(request)
        return ResponseEntity.noContent().build()
    }
}
