package com.pluxity.siteguard.constructionprogress.service

import com.pluxity.siteguard.constructionprogress.dto.ConstructionProgressBulkRequest
import com.pluxity.siteguard.constructionprogress.dto.ConstructionProgressResponse
import com.pluxity.siteguard.constructionprogress.dto.ConstructionProgressSearch
import com.pluxity.siteguard.constructionprogress.dto.toResponse
import com.pluxity.siteguard.constructionprogress.entity.ConstructionProgress
import com.pluxity.siteguard.constructionprogress.repository.ConstructionProgressRepository
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConstructionProgressService(
    private val repository: ConstructionProgressRepository,
    private val workTypeService: WorkTypeService,
) {
    @Transactional(readOnly = true)
    fun findAll(request: ConstructionProgressSearch): PageResponse<ConstructionProgressResponse> {
        val pageable =
            PageRequest.of(
                request.page - 1,
                request.size,
            )

        val page =
            repository.findPage(pageable) {
                select(entity(ConstructionProgress::class))
                    .from(entity(ConstructionProgress::class))
                    .orderBy(path(ConstructionProgress::workDate).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    @Transactional
    fun saveOrUpdateAll(request: ConstructionProgressBulkRequest) {
        // Delete
        if (request.deletedIds.isNotEmpty()) {
            repository.deleteAllById(request.deletedIds)
        }

        // Upsert
        request.upserts.forEach { item ->
            val workType = workTypeService.getById(item.workTypeId)

            item.id?.let { id ->
                repository
                    .findByIdOrNull(id)
                    ?.apply {
                        update(
                            workDate = item.workDate,
                            workType = workType,
                            plannedRate = item.plannedRate,
                            actualRate = item.actualRate,
                        )
                    }
                    ?: throw CustomException(ErrorCode.NOT_FOUND_CONSTRUCTION_PROGRESS, id)
            } ?: repository.save(
                ConstructionProgress(
                    workDate = item.workDate,
                    workType = workType,
                    plannedRate = item.plannedRate,
                    actualRate = item.actualRate,
                ),
            )
        }
    }
}
