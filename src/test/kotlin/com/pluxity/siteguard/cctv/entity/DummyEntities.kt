package com.pluxity.siteguard.cctv.entity

import com.pluxity.siteguard.base.entity.withAudit
import com.pluxity.siteguard.base.entity.withId

fun dummyCctv(
    id: Long? = null,
    path: String = "cam1",
    name: String? = null,
    lon: Double? = null,
    lat: Double? = null,
    isFavorite: Boolean = false,
) = Cctv(
    path = path,
    name = name,
    lon = lon,
    lat = lat,
    isFavorite = isFavorite,
).withId(id).withAudit()
