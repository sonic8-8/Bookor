package com.smhrd.bookor

data class Book(
    val id: Int,  // 고유 식별자 추가
    val title: String,
    val pages: Int,
    val review: String
)
