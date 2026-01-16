package com.pluxity.siteguard.targetmanagement.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.targetmanagement.dto.GoalBulkRequest
import com.pluxity.siteguard.targetmanagement.dto.GoalResponse
import com.pluxity.siteguard.targetmanagement.dto.GoalSearch
import com.pluxity.siteguard.targetmanagement.dto.toEntity
import com.pluxity.siteguard.targetmanagement.dto.toResponse
import com.pluxity.siteguard.targetmanagement.entity.Goal
import com.pluxity.siteguard.targetmanagement.repository.GoalRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val repository: GoalRepository,
    private val constructionSectionService: ConstructionSectionService,
) {
    @Transactional(readOnly = true)
    fun findAll(request: GoalSearch): PageResponse<GoalResponse> {
        val pageable = PageRequest.of(request.page - 1, request.size)

        val page =
            repository.findPage(pageable) {
                select(entity(Goal::class))
                    .from(entity(Goal::class))
                    .orderBy(path(Goal::inputDate).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    @Transactional
    fun saveOrUpdateAll(request: GoalBulkRequest) {
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
                    ?: throw CustomException(ErrorCode.NOT_FOUND_GOAL, id)
            } ?: repository.save(item.toEntity(constructionSection))
        }
    }
}
