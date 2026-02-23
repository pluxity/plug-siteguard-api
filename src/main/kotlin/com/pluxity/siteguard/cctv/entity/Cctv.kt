package com.pluxity.siteguard.cctv.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "cctv_stream")
class Cctv(
    @Column(name = "path", nullable = false, unique = true)
    val path: String,
    @Column(name = "name")
    var name: String? = null,
    @Column(name = "lon")
    var lon: Double? = null,
    @Column(name = "lat")
    var lat: Double? = null,
    @Column(name = "is_favorite", nullable = false)
    @ColumnDefault("false")
    var isFavorite: Boolean = false,
) : IdentityIdEntity() {
    fun update(
        name: String?,
        lon: Double?,
        lat: Double?,
    ) {
        this.name = name
        this.lon = lon
        this.lat = lat
    }

    fun markFavorite() {
        this.isFavorite = true
    }

    fun unmarkFavorite() {
        this.isFavorite = false
    }
}
