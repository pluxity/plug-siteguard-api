package com.pluxity.siteguard.notice.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.notice.dto.NoticeRequest
import com.pluxity.siteguard.notice.dto.NoticeResponse
import com.pluxity.siteguard.notice.dto.NoticeSearch
import com.pluxity.siteguard.notice.dto.toEntity
import com.pluxity.siteguard.notice.dto.toResponse
import com.pluxity.siteguard.notice.entity.Notice
import com.pluxity.siteguard.notice.repository.NoticeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(
    private val repository: NoticeRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(request: NoticeSearch): PageResponse<NoticeResponse> {
        val pageable = PageRequest.of(request.page - 1, request.size)

        val page =
            repository.findPage(pageable) {
                select(entity(Notice::class))
                    .from(entity(Notice::class))
                    .orderBy(path(Notice::id).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): NoticeResponse = getById(id).toResponse()

    @Transactional
    fun create(request: NoticeRequest): Long = repository.save(request.toEntity()).requiredId

    @Transactional
    fun update(
        id: Long,
        request: NoticeRequest,
    ) {
        getById(id).update(request.title, request.content)
    }

    @Transactional
    fun delete(id: Long) {
        repository.deleteById(getById(id).requiredId)
    }

    private fun getById(id: Long): Notice =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_NOTICE, id)
}
