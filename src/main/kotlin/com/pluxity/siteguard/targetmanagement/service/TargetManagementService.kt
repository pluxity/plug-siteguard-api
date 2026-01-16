package com.pluxity.siteguard.targetmanagement.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementBulkRequest
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementResponse
import com.pluxity.siteguard.targetmanagement.dto.TargetManagementSearch
import com.pluxity.siteguard.targetmanagement.dto.toEntity
import com.pluxity.siteguard.targetmanagement.dto.toResponse
import com.pluxity.siteguard.targetmanagement.entity.TargetManagement
import com.pluxity.siteguard.targetmanagement.repository.TargetManagementRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TargetManagementService(
    private val repository: TargetManagementRepository,
    private val constructionSectionService: ConstructionSectionService,
) {
    @Transactional(readOnly = true)
    fun findAll(request: TargetManagementSearch): PageResponse<TargetManagementResponse> {
        val pageable = PageRequest.of(request.page - 1, request.size)

        val page =
            repository.findPage(pageable) {
                select(entity(TargetManagement::class))
                    .from(entity(TargetManagement::class))
                    .orderBy(path(TargetManagement::inputDate).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    @Transactional
    fun saveOrUpdateAll(request: TargetManagementBulkRequest) {
        // Delete
        if (request.deletedIds.isNotEmpty()) {
            repository.deleteAllById(request.deletedIds)
        }

        // Upsert
        request.upserts.forEach { item ->
            val constructionSection = constructionSectionService.getById(item.constructionSectionId)

            item.id?.let { id ->
                repository
                    .findByIdOrNull(id)
                    ?.apply {
                        update(
                            constructionSection = constructionSection,
                            progressRate = item.progressRate,
                            constructionRate = item.constructionRate,
                            totalQuantity = item.totalQuantity,
                            cumulativeQuantity = item.cumulativeQuantity,
                            previousCumulativeQuantity = item.previousCumulativeQuantity,
                            targetQuantity = item.targetQuantity,
                            workQuantity = item.workQuantity,
                            startDate = item.startDate,
                            completionDate = item.completionDate,
                            plannedWorkDays = item.plannedWorkDays,
                            delayDays = item.delayDays,
                        )
                    }
                    ?: throw CustomException(ErrorCode.NOT_FOUND_TARGET_MANAGEMENT, id)
            } ?: repository.save(item.toEntity(constructionSection))
        }
    }
}
