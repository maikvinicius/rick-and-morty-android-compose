package com.example.rickandmorty.domain.model.response

import com.example.rickandmorty.domain.model.CharacterModel
import com.example.rickandmorty.domain.model.InfoModel
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseModel(
    val info: InfoModel,
    val results: List<CharacterModel>
)