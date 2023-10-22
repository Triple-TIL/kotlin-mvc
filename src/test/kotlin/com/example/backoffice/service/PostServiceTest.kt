package com.example.backoffice.service

import com.example.backoffice.domain.Post
import com.example.backoffice.exception.PostNotDeletableException
import com.example.backoffice.exception.PostNotFoundException
import com.example.backoffice.exception.PostNotUpdatableException
import com.example.backoffice.repository.PostRepository
import com.example.backoffice.service.dto.PostCreateRequestDto
import com.example.backoffice.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("게시글 생성자") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "test"
                )
            )

            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "test"
            }
        }
    }

    given("게시글 수정자") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "test"))
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id, PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "test"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
                updated?.updatedBy shouldBe "test"
            }
        }
        When("게시글이 없을 떄") {
            then("게시글을 찾을수 없다라는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "test"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시글입니다. 에러가 발생한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update test"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제시") {
        When("정상 삭제시") {
            val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "test"))
            val postId = postService.deletePost(saved.id, "test")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "test"))
            then("삭제할 수 없는 게시물 예외가 발생한다.") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved.id, "delete test")
                }
            }
        }
    }
})
