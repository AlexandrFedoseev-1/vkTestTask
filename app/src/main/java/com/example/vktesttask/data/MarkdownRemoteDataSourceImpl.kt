package com.example.vktesttask.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MarkdownRemoteDataSourceImpl : MarkdownRemoteDataSource {
    override suspend fun loadFromUrl(url: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"

                val inputStream = connection.inputStream
                val text = inputStream.bufferedReader().use { it.readText() }

                Result.success(text)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
