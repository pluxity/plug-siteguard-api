package com.pluxity.siteguard.permission

import org.springframework.data.jpa.repository.JpaRepository

interface DomainPermissionRepository : JpaRepository<DomainPermission, Long>
