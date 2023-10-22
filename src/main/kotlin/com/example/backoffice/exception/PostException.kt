package com.example.backoffice.exception

open class PostNotFoundException(): RuntimeException("게시글을 찾을 수 없다.")

open class PostNotUpdatableException: RuntimeException("게시글을 수정할 수 없습니다.")

open class PostNotDeletableException: RuntimeException("게시글을 삭제할 수 없습니다.")
