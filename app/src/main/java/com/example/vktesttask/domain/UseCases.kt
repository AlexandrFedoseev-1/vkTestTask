package com.example.vktesttask.domain


import android.net.Uri
import com.example.vktesttask.data.MarkdownParser

class LoadMarkdownFromUrlUseCase(private val repository: MarkdownRepository) {
    suspend operator fun invoke(url: String): Result<String> {
        return repository.loadFromUrl(url)
    }
}

class LoadMarkdownFromFileUseCase(private val repository: MarkdownRepository) {
    suspend operator fun invoke(path: String): Result<String> {
        return repository.loadFromFile(path)
    }
}

class SaveMarkdownToFileUseCase(private val repository: MarkdownRepository) {
    suspend operator fun invoke(uri: Uri, content: String): Result<Unit> {
        return repository.saveToFile(uri, content)
    }
}

class ParseMarkdownUseCase {
    fun invoke(content: String): MarkdownDocument {
        return MarkdownParser().parse(content)
    }
}