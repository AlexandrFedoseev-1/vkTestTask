package com.example.vktesttask.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktesttask.domain.SaveMarkdownToFileUseCase
import kotlinx.coroutines.launch

class SaveMarkdownViewModel (
    private val saveMarkdown: SaveMarkdownToFileUseCase
) : ViewModel() {

    fun save(uri: Uri, content: String) {
        viewModelScope.launch {
            saveMarkdown(uri, content)
        }
    }
}