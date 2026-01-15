package com.pluxity.siteguard.constructionprogress.service

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.pluxity.siteguard.constructionprogress.dto.dummyConstructionProgressBulkRequest
import com.pluxity.siteguard.constructionprogress.dto.dummyConstructionProgressRequest
import com.pluxity.siteguard.constructionprogress.dto.dummyConstructionProgressSearch
import com.pluxity.siteguard.constructionprogress.entity.ConstructionProgress
import com.pluxity.siteguard.constructionprogress.entity.dummyConstructionProgress
import com.pluxity.siteguard.constructionprogress.repository.ConstructionProgressRepository
import com.pluxity.siteguard.global.exception.CustomException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDate

class ConstructionProgressServiceTest :
    BehaviorSpec({

        val repository: ConstructionProgressRepository = mockk()
        val service = ConstructionProgressService(repository)

        Given("공정현황 조회") {

            When("전체 목록을 조회하면") {
                val entities =
                    listOf(
                        dummyConstructionProgress(id = 1L, phaseName = "터파기", workDate = LocalDate.of(2026, 1, 15)),
                        dummyConstructionProgress(id = 2L, phaseName = "기초공사", workDate = LocalDate.of(2026, 1, 14)),
                        dummyConstructionProgress(id = 3L, phaseName = "철근배근", workDate = LocalDate.of(2026, 1, 13)),
                    )
                val page =
                    PageImpl(
                        entities,
                        PageRequest.of(0, 10),
                        entities.size.toLong(),
                    )

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<ConstructionProgress>>>(),
                    )
                } returns page

                val result = service.findAll(dummyConstructionProgressSearch())

                Then("페이징된 결과가 반환된다") {
                    result.content.size shouldBe 3
                    result.pageNumber shouldBe 1
                    result.pageSize shouldBe 10
                    result.totalElements shouldBe 3
                }
            }

            When("빈 목록을 조회하면") {
                val pageable = PageRequest.of(0, 10)
                val page = PageImpl(emptyList<ConstructionProgress>(), pageable, 0)

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<
                            Jpql.() ->
                            JpqlQueryable<SelectQuery<ConstructionProgress>>,
                        >(),
                    )
                } returns page

                val result = service.findAll(dummyConstructionProgressSearch())

                Then("빈 결과가 반환된다") {
                    result.content.size shouldBe 0
                    result.totalElements shouldBe 0
                }
            }

            When("페이지 번호를 지정하여 조회하면") {
                val entities = (11L..15L).map { dummyConstructionProgress(id = it, phaseName = "콘크리트타설") }
                val page = PageImpl(entities, PageRequest.of(1, 10), 15)

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<ConstructionProgress>>>(),
                    )
                } returns page

                val result = service.findAll(dummyConstructionProgressSearch(page = 2))

                Then("해당 페이지의 결과가 반환된다") {
                    result.content.size shouldBe 5
                    result.pageNumber shouldBe 2
                    result.totalElements shouldBe 15
                    result.first shouldBe false
                }
            }
        }

        Given("공정현황 일괄 저장/수정/삭제") {

            When("id가 없는 데이터를 저장하면") {
                val request =
                    dummyConstructionProgressBulkRequest(
                        upserts = listOf(dummyConstructionProgressRequest()),
                    )

                every { repository.save(any()) } returns dummyConstructionProgress()

                service.saveOrUpdateAll(request)

                Then("repository.save가 호출된다") {
                    verify(exactly = 1) { repository.save(any()) }
                }
            }

            When("id가 있는 데이터를 수정하면") {
                val existingEntity =
                    dummyConstructionProgress(
                        id = 1L,
                        workDate = LocalDate.of(2026, 1, 10),
                        plannedRate = 80.0f,
                        actualRate = 75.0f,
                    )

                val request =
                    dummyConstructionProgressBulkRequest(
                        upserts =
                            listOf(
                                dummyConstructionProgressRequest(
                                    id = 1L,
                                    phaseName = "터파기 완료",
                                ),
                            ),
                    )

                every { repository.findById(1L) } returns java.util.Optional.of(existingEntity)

                service.saveOrUpdateAll(request)

                Then("엔티티가 업데이트된다") {
                    existingEntity.phaseName shouldBe "터파기 완료"
                    existingEntity.plannedRate shouldBe 100.0f
                    existingEntity.actualRate shouldBe 100.0f
                }
            }

            When("존재하지 않는 id로 수정하면") {
                val request =
                    dummyConstructionProgressBulkRequest(
                        upserts =
                            listOf(
                                dummyConstructionProgressRequest(id = 999L, phaseName = "없는 공정"),
                            ),
                    )

                every { repository.findById(999L) } returns java.util.Optional.empty()

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.saveOrUpdateAll(request)
                    }
                }
            }

            When("삭제할 id 목록이 있으면") {
                val request =
                    dummyConstructionProgressBulkRequest(
                        deletedIds = listOf(1L, 2L, 3L),
                    )

                every { repository.deleteAllById(listOf(1L, 2L, 3L)) } just runs

                service.saveOrUpdateAll(request)

                Then("repository.deleteAllById가 호출된다") {
                    verify(exactly = 1) { repository.deleteAllById(listOf(1L, 2L, 3L)) }
                }
            }

            When("저장, 수정, 삭제가 동시에 요청되면") {
                val existingEntity =
                    dummyConstructionProgress(
                        id = 2L,
                        phaseName = "기초공사",
                        plannedRate = 50.0f,
                        actualRate = 45.0f,
                    )

                val request =
                    dummyConstructionProgressBulkRequest(
                        upserts =
                            listOf(
                                dummyConstructionProgressRequest(phaseName = "신규 공정"),
                                dummyConstructionProgressRequest(id = 2L, phaseName = "기초공사 완료"),
                            ),
                        deletedIds = listOf(5L, 6L),
                    )

                every { repository.deleteAllById(listOf(5L, 6L)) } just runs
                every { repository.save(any()) } returns existingEntity
                every { repository.findById(2L) } returns java.util.Optional.of(existingEntity)

                service.saveOrUpdateAll(request)

                Then("삭제, 저장, 수정이 모두 수행된다") {
                    verify(exactly = 1) { repository.deleteAllById(listOf(5L, 6L)) }
                    verify(exactly = 1) { repository.save(any()) }
                    existingEntity.phaseName shouldBe "기초공사 완료"
                }
            }
        }
    })
