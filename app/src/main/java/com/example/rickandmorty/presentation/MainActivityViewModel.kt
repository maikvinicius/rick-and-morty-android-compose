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

    init {
        viewModelScope.launch {
            try {
                val response = repository.getAllCharacters()
                _state.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
