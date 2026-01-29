package com.pluxity.siteguard.notice.service

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.pluxity.siteguard.global.dto.PageSearchRequest
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.notice.dto.dummyNoticeRequest
import com.pluxity.siteguard.notice.entity.Notice
import com.pluxity.siteguard.notice.entity.dummyNotice
import com.pluxity.siteguard.notice.repository.NoticeRepository
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
import org.springframework.data.repository.findByIdOrNull

class NoticeServiceTest :
    BehaviorSpec({

        val repository: NoticeRepository = mockk()
        val service = NoticeService(repository)

        Given("공지사항 전체 조회") {

            When("공지사항 목록을 조회하면") {
                val entities =
                    listOf(
                        dummyNotice(id = 1L, title = "공지사항 1"),
                        dummyNotice(id = 2L, title = "공지사항 2"),
                        dummyNotice(id = 3L, title = "공지사항 3"),
                    )
                val pageable = PageRequest.of(0, 9999)
                val page = PageImpl(entities, pageable, entities.size.toLong())

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<Notice>>>(),
                    )
                } returns page

                val result = service.findAll(PageSearchRequest(page = 1, size = 9999))

                Then("페이징된 결과가 반환된다") {
                    result.content.size shouldBe 3
                    result.totalElements shouldBe 3
                    result.pageNumber shouldBe 1
                }
            }
        }

        Given("공지사항 상세 조회") {

            When("존재하는 ID로 조회하면") {
                val entity = dummyNotice(id = 1L, title = "테스트 공지사항")

                every { repository.findByIdOrNull(1L) } returns entity

                val result = service.findById(1L)

                Then("해당 공지사항이 반환된다") {
                    result.id shouldBe 1L
                    result.title shouldBe "테스트 공지사항"
                }
            }

            When("존재하지 않는 ID로 조회하면") {
                every { repository.findByIdOrNull(999L) } returns null

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.findById(999L)
                    }
                }
            }
        }

        Given("공지사항 등록") {

            When("새 공지사항을 등록하면") {
                val request = dummyNoticeRequest(title = "새 공지사항")
                val savedEntity = dummyNotice(id = 1L, title = "새 공지사항")

                every { repository.save(any()) } returns savedEntity

                val result = service.create(request)

                Then("저장된 ID가 반환된다") {
                    result shouldBe 1L
                    verify(exactly = 1) { repository.save(any()) }
                }
            }
        }

        Given("공지사항 수정") {

            When("존재하는 공지사항을 수정하면") {
                val existingEntity = dummyNotice(id = 1L, title = "기존 제목")
                val request = dummyNoticeRequest(title = "수정된 제목")

                every { repository.findByIdOrNull(1L) } returns existingEntity

                service.update(1L, request)

                Then("공지사항이 수정된다") {
                    existingEntity.title shouldBe "수정된 제목"
                }
            }

            When("존재하지 않는 공지사항을 수정하면") {
                val request = dummyNoticeRequest()

                every { repository.findByIdOrNull(999L) } returns null

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.update(999L, request)
                    }
                }
            }
        }

        Given("공지사항 삭제") {

            When("존재하는 공지사항을 삭제하면") {
                val entity = dummyNotice(id = 1L)

                every { repository.findByIdOrNull(1L) } returns entity
                every { repository.deleteById(1L) } just runs

                service.delete(1L)

                Then("삭제가 수행된다") {
                    verify(exactly = 1) { repository.deleteById(1L) }
                }
            }

            When("존재하지 않는 공지사항을 삭제하면") {
                every { repository.findByIdOrNull(999L) } returns null

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.delete(999L)
                    }
                }
            }
        }
    })
