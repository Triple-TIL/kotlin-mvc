package com.example.backoffice.repository

import com.example.backoffice.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>
