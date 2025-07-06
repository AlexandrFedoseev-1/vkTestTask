package com.example.vktesttask.presentation.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vktesttask.data.MarkdownLocalDataSourceImpl
import com.example.vktesttask.data.MarkdownRemoteDataSourceImpl
import com.example.vktesttask.data.MarkdownRepositoryImpl
import com.example.vktesttask.domain.SaveMarkdownToFileUseCase

class SaveMarkdownViewModelFactory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MarkdownRepositoryImpl(
            MarkdownRemoteDataSourceImpl(),
            MarkdownLocalDataSourceImpl(contentResolver)
        )
        val saveUseCase = SaveMarkdownToFileUseCase(repository)
        return SaveMarkdownViewModel(saveUseCase) as T
    }
}