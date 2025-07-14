package com.example.rickandmorty.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.rickandmorty.presentation.ui.theme.RickAndMortyTheme
import coil3.compose.AsyncImage
import com.example.rickandmorty.di.appModule
import com.example.rickandmorty.domain.model.CharacterModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin{
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }

        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(it)
                    ) {
                        RickAndMortyCharacterList()
                    }
                }
            }
        }
    }
}

@Composable
fun RickAndMortyCharacterList() {
    val viewModel: MainActivityViewModel = koinViewModel()
    val characters by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

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

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(characters) { character ->
                CharacterCard(character = character)
                Spacer(modifier = Modifier.height(16.dp))
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
}

@Composable
fun CharacterCard(character: CharacterModel) {
    Card(
        modifier = Modifier
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
                Text(text = "Origem: ${character.origin.name}")
                Text(text = "Localização: ${character.location.name}")
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
