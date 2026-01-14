package com.pluxity.siteguard.global.utils

import org.springframework.data.domain.Sort

object SortUtils {
    val orderByCreatedAtDesc: Sort = Sort.by(Sort.Direction.DESC, "createdAt")
}
