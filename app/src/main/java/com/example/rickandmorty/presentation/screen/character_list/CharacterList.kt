package com.example.rickandmorty.presentation.screen.character_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.rickandmorty.domain.model.CharacterModel
import com.example.rickandmorty.presentation.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@OptIn(FlowPreview::class)
@Composable
fun CharacterList(navController: NavController) {
    val viewModel: CharacterListViewModel = koinViewModel()
    val characters by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(query) {
        snapshotFlow { query }
            .debounce(500)
            .distinctUntilChanged()
            .collect { name ->
                viewModel.searchCharacters(name = name)
            }
    }

    RickAndMortyTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(it)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text("Busque um personagem...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.LightGray,
                                unfocusedIndicatorColor = Color.LightGray,
                                cursorColor = Color.Gray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(characters) { character ->
                                CharacterCard(character = character) {
                                    navController.navigate("characterDetail/${character.id}")
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.9f))
                                .zIndex(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }

                LaunchedEffect(listState) {
                    snapshotFlow {
                        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        val totalItemsCount = listState.layoutInfo.totalItemsCount
                        lastVisibleItemIndex to totalItemsCount
                    }.collect { (lastVisible, total) ->
                        if (lastVisible >= total - 1 && !isLoading) {
                            viewModel.loadMoreCharacters()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: CharacterModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = character.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "${character.species} - ${character.gender}")
                Text(text = "Episódios: ${character.episode.size}")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (character.status) {
                        "Alive" -> Color.Green
                        "Dead" -> Color.Red
                        else -> Color.Gray
                    }

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = character.status)
                }
            }
        }
    }
}