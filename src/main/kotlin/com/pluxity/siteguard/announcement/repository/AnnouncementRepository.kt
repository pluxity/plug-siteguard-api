package com.pluxity.siteguard.announcement.repository

import com.pluxity.siteguard.announcement.entity.Announcement
import org.springframework.data.jpa.repository.JpaRepository

interface AnnouncementRepository : JpaRepository<Announcement, Long>
