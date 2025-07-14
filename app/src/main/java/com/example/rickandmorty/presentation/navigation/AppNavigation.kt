package com.example.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmorty.presentation.screen.character_detail.CharacterDetail
import com.example.rickandmorty.presentation.screen.character_list.CharacterList

object Routes {
    const val CHARACTER_LIST = "characterList"
    const val CHARACTER_DETAIL = "characterDetail/{characterId}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.CHARACTER_LIST) {
        composable(Routes.CHARACTER_LIST) {
            CharacterList(navController)
        }

        composable(Routes.CHARACTER_DETAIL) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull()
            characterId?.let {
                CharacterDetail(characterId = it) {
                    navController.popBackStack()
                }
            }
        }
    }
}