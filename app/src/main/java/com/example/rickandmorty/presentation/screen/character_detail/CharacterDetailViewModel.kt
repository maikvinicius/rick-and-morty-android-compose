package com.example.rickandmorty.presentation.screen.character_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.domain.model.CharacterModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow<CharacterModel?>(null)
    val state: StateFlow<CharacterModel?> = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getCharacterById(characterId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val character = repository.getCharacterById(characterId)
                _state.value = character
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}