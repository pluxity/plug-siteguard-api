package com.pluxity.siteguard.processstatus.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.processstatus.dto.ProcessStatusBulkRequest
import com.pluxity.siteguard.processstatus.dto.ProcessStatusResponse
import com.pluxity.siteguard.processstatus.dto.ProcessStatusSearch
import com.pluxity.siteguard.processstatus.dto.toResponse
import com.pluxity.siteguard.processstatus.entity.ProcessStatus
import com.pluxity.siteguard.processstatus.repository.ProcessStatusRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProcessStatusService(
    private val repository: ProcessStatusRepository,
    private val workTypeService: WorkTypeService,
) {
    @Transactional(readOnly = true)
    fun findAll(request: ProcessStatusSearch): PageResponse<ProcessStatusResponse> {
        val pageable = PageRequest.of(request.page - 1, request.size)

        val page =
            repository.findPage(pageable) {
                select(entity(ProcessStatus::class))
                    .from(entity(ProcessStatus::class))
                    .orderBy(path(ProcessStatus::workDate).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findLatest(): List<ProcessStatusResponse> = repository.findAllByLatestWorkDate().map { it.toResponse() }

    @Transactional
    fun saveOrUpdateAll(request: ProcessStatusBulkRequest) {
        // Delete
        if (request.deletedIds.isNotEmpty()) {
            repository.deleteAllById(request.deletedIds)
        }

        // Upsert
        request.upserts.forEach { item ->
            val workType = workTypeService.getById(item.workTypeId)

            if (item.id != null) {
                // 수정 시 중복 체크 (자기 자신 제외)
                if (repository.existsByWorkDateAndWorkTypeAndIdNot(item.workDate, workType, item.id)) {
                    throw CustomException(ErrorCode.DUPLICATE_PROCESS_STATUS, item.workDate, workType.name)
                }

                repository
                    .findByIdOrNull(item.id)
                    ?.apply {
                        update(
                            workDate = item.workDate,
                            workType = workType,
                            plannedRate = item.plannedRate,
                            actualRate = item.actualRate,
                        )
                    }
                    ?: throw CustomException(ErrorCode.NOT_FOUND_PROCESS_STATUS, item.id)
            } else {
                // 신규 등록 시 중복 체크
                if (repository.existsByWorkDateAndWorkType(item.workDate, workType)) {
                    throw CustomException(ErrorCode.DUPLICATE_PROCESS_STATUS, item.workDate, workType.name)
                }

                repository.save(
                    ProcessStatus(
                        workDate = item.workDate,
                        workType = workType,
                        plannedRate = item.plannedRate,
                        actualRate = item.actualRate,
                    ),
                )
            }
        }
    }
}
