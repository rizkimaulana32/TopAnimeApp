package com.c242ps518.favorite

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c242ps518.core.data.repository.UiState
import com.c242ps518.core.domain.model.Anime
import com.c242ps518.topanimeairing.ui.screen.home.AnimeListItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules

@Suppress("unused")
@Composable
fun FavoriteScreen(
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    loadKoinModules(favoriteModule)

    val viewModel: FavoriteViewModel = koinViewModel()

    val uiState by viewModel.uiState.observeAsState(initial = UiState.Loading)

//    val dataLoaded = rememberSaveable { mutableStateOf(false) }
//
//    LaunchedEffect(key1 = true) {
//        if (!dataLoaded.value) {
//            viewModel.getFavoriteAnime()
//            dataLoaded.value = true
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.getFavoriteAnime()
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
            if ((uiState as UiState.Success).data.isEmpty()) {
                NoFavoriteAnime(navigateBack = navigateBack)
            } else {
                FavoriteContent(
                    animeList = (uiState as UiState.Success).data,
                    navigateToDetail = navigateToDetail,
                    navigateBack = navigateBack,
                )
            }
        }

        is UiState.Error -> {
            val errorMessage = (uiState as UiState.Error).errorMessage
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }
}


@Composable
fun FavoriteContent(
    animeList: List<Anime>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    navigateBack: () -> Unit,
) {
    Column(modifier = modifier) {
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

            Text(text = "Favorite Anime", fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
               animeList
            ) { data ->
                AnimeListItem(
                    title = data.title ?: "Unknown Title",
                    score = data.score,
                    rank = data.rank,
                    imageUrl = data.imageUrl ?: "",
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable {
                            navigateToDetail(data.animeId)
                        }
                        .animateItem(placementSpec = tween(durationMillis = 100))
                )
            }

        }
    }
}

@Composable
fun NoFavoriteAnime(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
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

            Text(text = "Favorite Anime", fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No Favorite Anime")
        }
    }
}
