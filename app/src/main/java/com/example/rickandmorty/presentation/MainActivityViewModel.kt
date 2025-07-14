package com.example.rickandmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.CharacterModel
import kotlinx.coroutines.launch
import com.example.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow<List<CharacterModel>>(emptyList())
    val state: StateFlow<List<CharacterModel>> = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var endReached = false

    init {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getAllCharacters(currentPage = currentPage)
                _state.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreCharacters() {
        if (_isLoading.value || endReached) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getAllCharacters(currentPage = currentPage)
                val newCharacters = response.results
                if (newCharacters.isNotEmpty()) {
                    _state.value += newCharacters
                    currentPage++
                } else {
                    endReached = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchCharacters(name: String) {
        currentPage = 1
        endReached = false
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.searchCharacters(name = name, currentPage = currentPage)
                _state.value = response.results
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}
