package com.example.vktesttask.domain

import android.net.Uri

interface MarkdownRepository {
    suspend fun loadFromUrl(url: String): Result<String>
    suspend fun loadFromFile(path: String): Result<String>
    suspend fun saveToFile(uri: Uri, content: String): Result<Unit>
}