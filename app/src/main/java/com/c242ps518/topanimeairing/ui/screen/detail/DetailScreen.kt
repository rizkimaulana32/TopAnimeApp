package com.c242ps518.topanimeairing.ui.screen.detail


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.c242ps518.core.data.repository.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    animeId: Long,
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.observeAsState(initial = UiState.Loading)

    val currentAnimeId by rememberUpdatedState(animeId)

    LaunchedEffect(Unit) {
        viewModel.getAnimeById(currentAnimeId)
    }

    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val anime = (uiState as UiState.Success).data
            DetailContent(
                title = anime.title ?: "Unknown Titles",
                score = anime.score,
                rank = anime.rank,
                imageUrl = anime.imageUrl ?: "",
                synopsis = anime.synopsis ?: "",
                genres = anime.genres,
                isFavorite = anime.isFavorite,
                navigateBack = navigateBack,
                onFavoriteClick = { isFavorite ->
                    viewModel.setFavoriteAnime(anime, isFavorite)
                }
            )
        }

        is UiState.Error -> {
            val errorMessage = (uiState as UiState.Error).errorMessage
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
            navigateBack()
        }
    }
}


@Composable
fun DetailContent(
    title: String,
    score: Double?,
    rank: Int?,
    imageUrl: String,
    synopsis: String,
    genres: String?,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = navigateBack,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(text = "Detail Anime", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Score: ${score ?: "N/A"}")
                        Text(text = "Rank: ${rank ?: "N/A"}")
                        Text(text = "Genres: ${genres ?: "N/A"}")
                    }
                }
                Text(text = "Synopsis: $synopsis")
            }
        }

        FloatingActionButton(
            onClick = { onFavoriteClick(!isFavorite) },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite Button"
            )
        }
    }
}
