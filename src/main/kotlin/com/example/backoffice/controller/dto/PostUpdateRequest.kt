package com.example.backoffice.controller.dto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)
