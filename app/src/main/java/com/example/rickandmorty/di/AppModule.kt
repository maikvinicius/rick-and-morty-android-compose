package com.example.rickandmorty.di

import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickandmorty.presentation.screen.character_detail.CharacterDetailViewModel
import com.example.rickandmorty.presentation.screen.character_list.CharacterListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModules = module {
    single<CharacterRepository> { CharacterRepositoryImpl() }
}

val viewModelModules = module {
    viewModel { CharacterListViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}

val appModule = listOf(
    repositoryModules,
    viewModelModules
)