package com.example.rickandmorty.data.repository

import com.example.rickandmorty.domain.model.CharacterModel
import com.example.rickandmorty.domain.model.InfoModel
import com.example.rickandmorty.domain.model.LocationModel
import com.example.rickandmorty.domain.model.OriginModel
import com.example.rickandmorty.domain.model.response.CharacterResponseModel

class FakeCharacterRepository : CharacterRepository {
    val character = CharacterModel(
        id = 1,
        name = "Personagem Fake",
        status = "Alive",
        species = "Human",
        type = "",
        origin = OriginModel(
            name = "Origin Fake",
            url = ""
        ),
        location = LocationModel(
            name = "Location Fake",
            url = ""
        ),
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episode = emptyList(),
        url = "https://rickandmortyapi.com/api/character/1",
        created = "2017-11-04T18:50:21.651Z",
        gender = "Male"
    )

    private var callCount = 0

    override suspend fun getAllCharacters(currentPage: Int): CharacterResponseModel {
        callCount++
        return CharacterResponseModel(
            info = InfoModel(1, 1, null, null),
            results = listOf(
                CharacterModel(
                    id = callCount,
                    name = "Personagem Fake $callCount",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    origin = OriginModel("Origin Fake", ""),
                    location = LocationModel("Location Fake", ""),
                    image = "https://rickandmortyapi.com/api/character/avatar/$callCount.jpeg",
                    episode = emptyList(),
                    url = "https://rickandmortyapi.com/api/character/$callCount",
                    created = "2017-11-04T18:50:21.651Z",
                    gender = "Male"
                )
            )
        )
    }

    override suspend fun searchCharacters(
        name: String,
        currentPage: Int
    ): CharacterResponseModel {
        return CharacterResponseModel(
            info = InfoModel(
                count = 1,
                pages = 1,
                next = null,
                prev = null
            ),
            results = listOf(character)
        )
    }

    override suspend fun getCharacterById(characterId: Int): CharacterModel {
        return character
    }
}