package com.pluxity.siteguard.file.repository

import com.pluxity.siteguard.file.entity.ZipContentEntry
import org.springframework.data.jpa.repository.JpaRepository

interface ZipContentEntryRepository : JpaRepository<ZipContentEntry, Long> {
    fun findByFileId(fileId: Long): List<ZipContentEntry>

    fun findByFileIdIn(fileIds: List<Long>): List<ZipContentEntry>
}
