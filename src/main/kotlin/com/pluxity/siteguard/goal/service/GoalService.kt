package com.pluxity.siteguard.goal.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.global.utils.findAllByIdsOrThrow
import com.pluxity.siteguard.goal.dto.GoalBulkRequest
import com.pluxity.siteguard.goal.dto.GoalResponse
import com.pluxity.siteguard.goal.dto.GoalSearch
import com.pluxity.siteguard.goal.dto.toEntity
import com.pluxity.siteguard.goal.dto.toResponse
import com.pluxity.siteguard.goal.entity.Goal
import com.pluxity.siteguard.goal.repository.ConstructionSectionRepository
import com.pluxity.siteguard.goal.repository.GoalRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GoalService(
    private val repository: GoalRepository,
    private val constructionSectionRepository: ConstructionSectionRepository,
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

    @Transactional(readOnly = true)
    fun findLatest(): List<GoalResponse> = repository.findAllByLatestInputDate().map { it.toResponse() }

    @Transactional
    fun saveOrUpdateAll(request: GoalBulkRequest) {
        // Delete
        if (request.deletedIds.isNotEmpty()) {
            repository.deleteAllById(request.deletedIds)
        }

        if (request.upserts.isEmpty()) return

        // ConstructionSection 한번에 조회
        val constructionSectionIds = request.upserts.map { it.constructionSectionId }.distinct()
        val constructionSectionMap =
            findAllByIdsOrThrow(
                ids = constructionSectionIds,
                findAllById = constructionSectionRepository::findAllById,
                idExtractor = { it.requiredId },
                errorCode = ErrorCode.NOT_FOUND_CONSTRUCTION_SECTION,
            )

        // 수정할 Goal 한번에 조회
        val updateIds = request.upserts.mapNotNull { it.id }
        val goalMap =
            findAllByIdsOrThrow(
                ids = updateIds,
                findAllById = repository::findAllById,
                idExtractor = { it.requiredId },
                errorCode = ErrorCode.NOT_FOUND_GOAL,
            )

        // Upsert
        request.upserts.forEach { item ->
            val constructionSection =
                constructionSectionMap[item.constructionSectionId]
                    ?: throw CustomException(ErrorCode.NOT_FOUND_CONSTRUCTION_SECTION, item.constructionSectionId)

            if (item.id == null) {
                repository.save(item.toEntity(constructionSection))
                return@forEach
            }

            (goalMap[item.id] ?: throw CustomException(ErrorCode.NOT_FOUND_GOAL, item.id))
                .update(
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
    }
}
