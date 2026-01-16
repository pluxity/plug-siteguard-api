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
import com.pluxity.siteguard.processstatus.entity.WorkType
import com.pluxity.siteguard.processstatus.repository.ProcessStatusRepository
import com.pluxity.siteguard.processstatus.repository.WorkTypeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProcessStatusService(
    private val repository: ProcessStatusRepository,
    private val workTypeRepository: WorkTypeRepository,
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

        if (request.upserts.isEmpty()) return

        // WorkType 한번에 조회
        val workTypeIds = request.upserts.map { it.workTypeId }.distinct()
        val workTypeMap = getWorkTypeMapByIds(workTypeIds)

        // 수정할 ProcessStatus 한번에 조회
        val updateIds = request.upserts.mapNotNull { it.id }
        val processStatusMap = getProcessStatusMapByIds(updateIds)

        // Upsert
        request.upserts.forEach { item ->
            val workType =
                workTypeMap[item.workTypeId]
                    ?: throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, item.workTypeId)

            if (item.id == null) {
                repository.save(
                    ProcessStatus(
                        workDate = item.workDate,
                        workType = workType,
                        plannedRate = item.plannedRate,
                        actualRate = item.actualRate,
                    ),
                )
                return@forEach
            }

            (processStatusMap[item.id] ?: throw CustomException(ErrorCode.NOT_FOUND_PROCESS_STATUS, item.id))
                .update(
                    workDate = item.workDate,
                    workType = workType,
                    plannedRate = item.plannedRate,
                    actualRate = item.actualRate,
                )
        }
    }

    private fun getWorkTypeMapByIds(ids: List<Long>): Map<Long, WorkType> {
        if (ids.isEmpty()) return emptyMap()

        val workTypes = workTypeRepository.findAllById(ids)
        val foundIds = workTypes.map { it.requiredId }.toSet()
        val notFoundIds = ids.filter { it !in foundIds }
        if (notFoundIds.isNotEmpty()) {
            throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, notFoundIds.first())
        }
        return workTypes.associateBy { it.requiredId }
    }

    private fun getProcessStatusMapByIds(ids: List<Long>): Map<Long, ProcessStatus> {
        if (ids.isEmpty()) return emptyMap()

        val processStatuses = repository.findAllById(ids)
        val foundIds = processStatuses.map { it.requiredId }.toSet()
        val notFoundIds = ids.filter { it !in foundIds }
        if (notFoundIds.isNotEmpty()) {
            throw CustomException(ErrorCode.NOT_FOUND_PROCESS_STATUS, notFoundIds)
        }
        return processStatuses.associateBy { it.requiredId }
    }
}
