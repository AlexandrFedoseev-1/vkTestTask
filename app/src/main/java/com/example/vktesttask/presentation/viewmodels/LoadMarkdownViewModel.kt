package com.example.vktesttask.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktesttask.domain.LoadMarkdownFromFileUseCase
import com.example.vktesttask.domain.LoadMarkdownFromUrlUseCase
import com.example.vktesttask.domain.MarkdownDocument
import com.example.vktesttask.domain.ParseMarkdownUseCase
import kotlinx.coroutines.launch

class LoadMarkdownViewModel(
    private val loadFromUrlUseCase: LoadMarkdownFromUrlUseCase,
    private val loadFromFileUseCase: LoadMarkdownFromFileUseCase,
    private val parseMarkdown: ParseMarkdownUseCase
) : ViewModel() {

    private val _state = MutableLiveData<LoadState>()
    val state: LiveData<LoadState> = _state

    fun loadFromUrl(url: String) {
        viewModelScope.launch {
            _state.value = LoadState.Loading
            val result = loadFromUrlUseCase(url)
            _state.value = result.fold(
                onSuccess = { LoadState.Success(parseMarkdown.invoke(it), it) },
                onFailure = { LoadState.Error(it.message.orEmpty()) }
            )
        }
    }

    fun loadFromFile(path: String) {
        viewModelScope.launch {
            _state.value = LoadState.Loading
            val result = loadFromFileUseCase(path)
            _state.value = result.fold(
                onSuccess = { LoadState.Success(parseMarkdown.invoke(it), it) },
                onFailure = { LoadState.Error(it.message.orEmpty()) }
            )
        }
    }

    fun parseMarkdown(content: String): MarkdownDocument{
        return parseMarkdown.invoke(content)
    }

    sealed class LoadState {
        object Loading : LoadState()
        data class Success(val document: MarkdownDocument, val rawText: String) : LoadState()
        data class Error(val message: String) : LoadState()
    }
}