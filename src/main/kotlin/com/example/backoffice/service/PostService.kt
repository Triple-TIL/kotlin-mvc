package com.example.backoffice.service

import com.example.backoffice.repository.PostRepository
import com.example.backoffice.service.dto.PostCreateRequestDto
import com.example.backoffice.service.dto.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository
) {

    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

}
