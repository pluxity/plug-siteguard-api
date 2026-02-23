package com.pluxity.siteguard.cctv.service

import com.pluxity.siteguard.cctv.client.CctvApiClient
import com.pluxity.siteguard.cctv.dto.CctvUpdateRequest
import com.pluxity.siteguard.cctv.dto.MediaServerPathItem
import com.pluxity.siteguard.cctv.entity.Cctv
import com.pluxity.siteguard.cctv.entity.dummyCctv
import com.pluxity.siteguard.cctv.repository.CctvRepository
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CctvServiceTest :
    BehaviorSpec({

        val repository: CctvRepository = mockk(relaxed = true)
        val apiClient: CctvApiClient = mockk()
        val service = CctvService(repository, apiClient)

        Given("CCTV 동기화") {

            When("미디어서버에 새로운 path가 있으면") {
                val externalPaths =
                    listOf(
                        MediaServerPathItem(name = "cam1", confName = "conf1", ready = true),
                        MediaServerPathItem(name = "cam2", confName = "conf2", ready = true),
                    )

                every { apiClient.fetchPaths() } returns externalPaths
                every { repository.findAll() } returns emptyList()

                service.sync()

                Then("새로운 CCTV가 저장된다") {
                    verify { repository.saveAll(match<List<Cctv>> { it.size == 2 }) }
                }
            }

            When("DB에 있지만 미디어서버에 없는 path가 있으면") {
                val existingCctv = dummyCctv(id = 1L, path = "cam_old")

                every { apiClient.fetchPaths() } returns
                    listOf(
                        MediaServerPathItem(name = "cam_new", confName = "conf1", ready = true),
                    )
                every { repository.findAll() } returns listOf(existingCctv)

                service.sync()

                Then("해당 CCTV가 삭제된다") {
                    verify { repository.deleteAllInBatch(match<List<Cctv>> { it.size == 1 && it[0].path == "cam_old" }) }
                }
            }

            When("미디어서버와 DB가 동일하면") {
                val existingCctv = dummyCctv(id = 1L, path = "cam1")

                every { apiClient.fetchPaths() } returns
                    listOf(
                        MediaServerPathItem(name = "cam1", confName = "conf1", ready = true),
                    )
                every { repository.findAll() } returns listOf(existingCctv)

                service.sync()

                Then("저장이나 삭제가 수행되지 않는다") {
                    verify(exactly = 0) { repository.saveAll(any<List<Cctv>>()) }
                    verify(exactly = 0) { repository.deleteAllInBatch(any<List<Cctv>>()) }
                }
            }
        }

        Given("CCTV 목록 조회") {

            When("CCTV 목록을 조회하면") {
                val entities =
                    listOf(
                        dummyCctv(id = 1L, path = "cam1", name = "1번 카메라", isFavorite = true),
                        dummyCctv(id = 2L, path = "cam2", name = "2번 카메라"),
                    )

                every { repository.findAllByOrderByIsFavoriteDescNameAsc() } returns entities

                val result = service.findAll()

                Then("목록이 반환된다") {
                    result.size shouldBe 2
                    result[0].isFavorite shouldBe true
                    result[1].isFavorite shouldBe false
                }
            }

            When("CCTV가 없으면") {
                every { repository.findAllByOrderByIsFavoriteDescNameAsc() } returns emptyList()

                val result = service.findAll()

                Then("빈 목록이 반환된다") {
                    result.size shouldBe 0
                }
            }
        }

        Given("CCTV 수정") {

            When("존재하는 CCTV를 수정하면") {
                val entity = dummyCctv(id = 1L, path = "cam1")
                val request = CctvUpdateRequest(name = "1번 카메라", lon = 127.0, lat = 37.0)

                every { repository.findByIdOrNull(1L) } returns entity

                service.update(1L, request)

                Then("name, lon, lat이 수정된다") {
                    entity.name shouldBe "1번 카메라"
                    entity.lon shouldBe 127.0
                    entity.lat shouldBe 37.0
                }
            }

            When("존재하지 않는 CCTV를 수정하면") {
                every { repository.findByIdOrNull(999L) } returns null

                Then("NOT_FOUND_CCTV 예외가 발생한다") {
                    val exception =
                        shouldThrow<CustomException> {
                            service.update(999L, CctvUpdateRequest(name = "test", lon = null, lat = null))
                        }
                    exception.errorCode shouldBe ErrorCode.NOT_FOUND_CCTV
                }
            }
        }

        Given("즐겨찾기 추가") {

            When("즐겨찾기가 아닌 CCTV에 추가하면") {
                val entity = dummyCctv(id = 1L, path = "cam1", isFavorite = false)

                every { repository.findByIdOrNull(1L) } returns entity
                every { repository.countByIsFavoriteTrue() } returns 2

                service.addFavorite(1L)

                Then("즐겨찾기가 설정된다") {
                    entity.isFavorite shouldBe true
                }
            }

            When("이미 즐겨찾기인 CCTV에 추가하면") {
                val entity = dummyCctv(id = 1L, path = "cam1", isFavorite = true)

                every { repository.findByIdOrNull(1L) } returns entity

                Then("ALREADY_FAVORITE 예외가 발생한다") {
                    val exception =
                        shouldThrow<CustomException> {
                            service.addFavorite(1L)
                        }
                    exception.errorCode shouldBe ErrorCode.ALREADY_FAVORITE
                }
            }

            When("즐겨찾기가 4개인 상태에서 추가하면") {
                val entity = dummyCctv(id = 5L, path = "cam5", isFavorite = false)

                every { repository.findByIdOrNull(5L) } returns entity
                every { repository.countByIsFavoriteTrue() } returns 4

                Then("EXCEED_FAVORITE_LIMIT 예외가 발생한다") {
                    val exception =
                        shouldThrow<CustomException> {
                            service.addFavorite(5L)
                        }
                    exception.errorCode shouldBe ErrorCode.EXCEED_FAVORITE_LIMIT
                }
            }
        }

        Given("즐겨찾기 해제") {

            When("즐겨찾기인 CCTV를 해제하면") {
                val entity = dummyCctv(id = 1L, path = "cam1", isFavorite = true)

                every { repository.findByIdOrNull(1L) } returns entity

                service.removeFavorite(1L)

                Then("즐겨찾기가 해제된다") {
                    entity.isFavorite shouldBe false
                }
            }

            When("즐겨찾기가 아닌 CCTV를 해제하면") {
                val entity = dummyCctv(id = 1L, path = "cam1", isFavorite = false)

                every { repository.findByIdOrNull(1L) } returns entity

                Then("NOT_FAVORITE 예외가 발생한다") {
                    val exception =
                        shouldThrow<CustomException> {
                            service.removeFavorite(1L)
                        }
                    exception.errorCode shouldBe ErrorCode.NOT_FAVORITE
                }
            }

            When("존재하지 않는 CCTV를 해제하면") {
                every { repository.findByIdOrNull(999L) } returns null

                Then("NOT_FOUND_CCTV 예외가 발생한다") {
                    val exception =
                        shouldThrow<CustomException> {
                            service.removeFavorite(999L)
                        }
                    exception.errorCode shouldBe ErrorCode.NOT_FOUND_CCTV
                }
            }
        }
    })
