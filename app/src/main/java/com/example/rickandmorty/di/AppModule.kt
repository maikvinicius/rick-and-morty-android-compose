package com.example.rickandmorty.di

import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickandmorty.presentation.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModules = module {
    single<CharacterRepository> { CharacterRepositoryImpl() }
}

val viewModelModules = module {
    viewModel { MainActivityViewModel(get()) }
}

val appModule = listOf(
    repositoryModules,
    viewModelModules
)