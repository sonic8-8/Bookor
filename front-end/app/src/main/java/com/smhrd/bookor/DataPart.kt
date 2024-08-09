package com.smhrd.bookor


data class DataPart(
    val fileName: String,
    val content: ByteArray,
    val type: String = "image/jpeg"
)