package com.pluxity.siteguard.targetmanagement.entity

enum class ConstructionSection(
    val description: String,
) {
    CUTTING("절토"),
    RETAINING_WALL("옹벽공"),
    ROAD("도로공"),
    STORM_DRAIN("우수공"),
    SEWAGE("오수공"),
    WATER_SUPPLY("상수공"),
    RIVER("하천공"),
    BRIDGE("교량공"),
    NON_OPEN_CUT("비개착공"),
    AUXILIARY("부대공"),
}
