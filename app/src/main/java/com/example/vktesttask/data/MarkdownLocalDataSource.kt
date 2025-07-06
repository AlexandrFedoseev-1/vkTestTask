package com.example.vktesttask.data

import android.net.Uri

interface MarkdownLocalDataSource {
    suspend fun loadFromFile(path: String): Result<String>
    suspend fun saveToFile(uri: Uri, content: String): Result<Unit>
}