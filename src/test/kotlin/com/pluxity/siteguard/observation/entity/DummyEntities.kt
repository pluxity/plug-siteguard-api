package com.pluxity.siteguard.observation.entity

import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyObservation(
    id: Long? = null,
    date: LocalDate = LocalDate.now(),
    description: String? = "테스트 설명",
    fileId: Long = 1L,
    rootFileName: String = "test_file",
    directoryPath: String? = null,
) = Observation(
    date = date,
    description = description,
    fileId = fileId,
    rootFileName = rootFileName,
    directoryPath = directoryPath,
).withId(id)
