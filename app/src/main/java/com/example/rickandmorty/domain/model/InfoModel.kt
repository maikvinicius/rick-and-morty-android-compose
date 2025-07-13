package com.example.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InfoModel(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)