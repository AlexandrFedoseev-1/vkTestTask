package com.example.vktesttask.data

interface MarkdownRemoteDataSource {
    suspend fun loadFromUrl(url: String): Result<String>
}