package com.example.vktesttask.presentation.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vktesttask.data.MarkdownLocalDataSourceImpl
import com.example.vktesttask.data.MarkdownRemoteDataSourceImpl
import com.example.vktesttask.data.MarkdownRepositoryImpl
import com.example.vktesttask.domain.LoadMarkdownFromFileUseCase
import com.example.vktesttask.domain.LoadMarkdownFromUrlUseCase
import com.example.vktesttask.domain.ParseMarkdownUseCase

class LoadMarkdownViewModelFactory(private val contentResolver: ContentResolver): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MarkdownRepositoryImpl(
            MarkdownRemoteDataSourceImpl(),
            MarkdownLocalDataSourceImpl(contentResolver)
        )
        val loadFromUrl = LoadMarkdownFromUrlUseCase(repository)
        val loadFromFile = LoadMarkdownFromFileUseCase(repository)
        val parse = ParseMarkdownUseCase()
        return LoadMarkdownViewModel(loadFromUrl, loadFromFile, parse) as T
    }
}