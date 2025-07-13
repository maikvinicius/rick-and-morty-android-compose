package com.example.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationModel(
    val name: String,
    val url: String
)