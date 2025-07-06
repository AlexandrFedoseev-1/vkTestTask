package com.example.vktesttask.data

import android.net.Uri
import com.example.vktesttask.domain.MarkdownRepository

class MarkdownRepositoryImpl (
    private val remoteSource: MarkdownRemoteDataSource,
    private val localSource: MarkdownLocalDataSource
) : MarkdownRepository {

    override suspend fun loadFromUrl(url: String): Result<String> {
        return remoteSource.loadFromUrl(url)
    }

    override suspend fun loadFromFile(path: String): Result<String> {
        return localSource.loadFromFile(path)
    }

    override suspend fun saveToFile(uri: Uri, content: String): Result<Unit> {
        return localSource.saveToFile(uri, content)
    }
}