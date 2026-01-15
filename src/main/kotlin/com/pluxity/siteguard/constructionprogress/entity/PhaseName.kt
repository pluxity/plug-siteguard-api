package com.pluxity.siteguard.constructionprogress.entity

enum class PhaseName(
    val description: String,
) {
    EARTHWORK("토공"),
    ROAD("도로공"),
    NON_OPEN_CUT("비개착"),
    BRIDGE_RETAINING_WALL("교량/옹벽"),
    TOTAL("전체"),
}
