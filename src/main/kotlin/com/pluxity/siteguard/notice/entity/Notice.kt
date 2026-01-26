package com.pluxity.siteguard.notice.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "notice")
class Notice(
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", length = 1000)
    var content: String,
) : IdentityIdEntity() {
    fun update(
        title: String,
        content: String,
    ) {
        this.title = title
        this.content = content
    }
}
