package com.example.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OriginModel(
    val name: String,
    val url: String
)