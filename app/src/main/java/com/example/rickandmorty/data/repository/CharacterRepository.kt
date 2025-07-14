package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.api.HttpClientProvider
import com.example.rickandmorty.domain.model.response.CharacterResponseModel
import io.ktor.client.call.body
import io.ktor.client.request.get

interface CharacterRepository {
    suspend fun getAllCharacters(currentPage: Int): CharacterResponseModel
}

class CharacterRepositoryImpl : CharacterRepository {
    override suspend fun getAllCharacters(currentPage: Int): CharacterResponseModel {
        return HttpClientProvider.client
            .get("character?page=$currentPage")
            .body()
    }
}