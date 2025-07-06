package com.example.vktesttask.data

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MarkdownLocalDataSourceImpl(
    private val contentResolver: ContentResolver
) : MarkdownLocalDataSource {
    override suspend fun loadFromFile(path: String): Result<String> {
        return try {
            val file = File(path)
            val text = file.readText()
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveToFile(uri: Uri, content: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                contentResolver.openOutputStream(uri)?.use { output ->
                    output.write(content.toByteArray())
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}