package com.pluxity.siteguard.notice.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.dto.PageSearchRequest
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.response.PageResponse
import com.pluxity.siteguard.global.response.toPageResponse
import com.pluxity.siteguard.global.utils.findAllNotNull
import com.pluxity.siteguard.global.utils.findPageNotNull
import com.pluxity.siteguard.notice.dto.NoticeRequest
import com.pluxity.siteguard.notice.dto.NoticeResponse
import com.pluxity.siteguard.notice.dto.toResponse
import com.pluxity.siteguard.notice.entity.Notice
import com.pluxity.siteguard.notice.repository.NoticeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class NoticeService(
    private val repository: NoticeRepository,
) {
    fun findAll(request: PageSearchRequest): PageResponse<NoticeResponse> {
        val pageable = PageRequest.of(request.page - 1, request.size)

        val page =
            repository.findPageNotNull(pageable) {
                select(entity(Notice::class))
                    .from(entity(Notice::class))
                    .orderBy(path(Notice::id).desc())
            }
        return page.toPageResponse { it.toResponse() }
    }

    fun findById(id: Long): NoticeResponse = getById(id).toResponse()

    fun findActive(): List<NoticeResponse> {
        val today = LocalDate.now()
        return repository
            .findAllNotNull {
                select(entity(Notice::class))
                    .from(entity(Notice::class))
                    .where(
                        path(Notice::isVisible)
                            .equal(true)
                            .and(
                                path(Notice::isAlways)
                                    .equal(true)
                                    .or(
                                        path(Notice::startDate)
                                            .lessThanOrEqualTo(today)
                                            .and(path(Notice::endDate).greaterThanOrEqualTo(today)),
                                    ),
                            ),
                    ).orderBy(path(Notice::id).desc())
            }.map { it.toResponse() }
    }

    @Transactional
    fun create(request: NoticeRequest): Long =
        repository
            .save(
                Notice(
                    title = request.title,
                    content = request.content,
                    isVisible = request.isVisible,
                    isAlways = request.isAlways,
                    startDate = request.startDate,
                    endDate = request.endDate,
                ),
            ).requiredId

    @Transactional
    fun update(
        id: Long,
        request: NoticeRequest,
    ) = getById(id).update(
        title = request.title,
        content = request.content,
        isVisible = request.isVisible,
        isAlways = request.isAlways,
        startDate = request.startDate,
        endDate = request.endDate,
    )

    @Transactional
    fun delete(id: Long) {
        repository.deleteById(getById(id).requiredId)
    }

    private fun getById(id: Long): Notice =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_NOTICE, id)
}
