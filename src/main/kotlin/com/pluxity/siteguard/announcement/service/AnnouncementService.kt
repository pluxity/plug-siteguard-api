package com.pluxity.siteguard.announcement.service

import com.pluxity.siteguard.announcement.dto.AnnouncementRequest
import com.pluxity.siteguard.announcement.dto.AnnouncementResponse
import com.pluxity.siteguard.announcement.dto.toResponse
import com.pluxity.siteguard.announcement.entity.Announcement
import com.pluxity.siteguard.announcement.repository.AnnouncementRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnnouncementService(
    private val repository: AnnouncementRepository,
) {
    @Transactional(readOnly = true)
    fun getAnnouncement(): AnnouncementResponse =
        repository.findByIdOrNull(Announcement.SINGLETON_ID)?.toResponse() ?: AnnouncementResponse()

    @Transactional
    fun saveAnnouncement(request: AnnouncementRequest) {
        repository
            .findByIdOrNull(Announcement.SINGLETON_ID)
            ?.apply { update(request.content) }
            ?: repository.save(Announcement(content = request.content))
    }
}
