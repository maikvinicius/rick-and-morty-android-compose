package com.example.rickandmorty.presentation.screen.character_list

import com.example.rickandmorty.data.repository.FakeCharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val fakeRepository = FakeCharacterRepository()
        viewModel = CharacterListViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchCharacters should update state with characters`() = runTest {
        viewModel.searchCharacters("Rick")
        testDispatcher.scheduler.advanceUntilIdle()

        val expected = listOf(FakeCharacterRepository().character)
        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `loadMoreCharacters should append characters`() = runTest {
        viewModel.loadMoreCharacters()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadMoreCharacters()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(3, viewModel.state.value.size)
        assertEquals("Personagem Fake 1", viewModel.state.value[0].name)
        assertEquals("Personagem Fake 2", viewModel.state.value[1].name)
        assertEquals("Personagem Fake 3", viewModel.state.value[2].name)
    }

    @Test
    fun `getCharacterById should return correct character`() = runTest {
        val repository = FakeCharacterRepository()

        val result = repository.getCharacterById(1)

        assertEquals(1, result.id)
        assertEquals("Personagem Fake", result.name)
        assertEquals("Alive", result.status)
        assertEquals("Human", result.species)
        assertEquals("Male", result.gender)
        assertEquals("Origin Fake", result.origin.name)
        assertEquals("Location Fake", result.location.name)
    }
}