package com.c242ps518.topanimeairing.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.c242ps518.topanimeairing.R
import com.c242ps518.core.data.repository.UiState
import com.c242ps518.core.domain.model.Anime
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    navigateToFavorite: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.observeAsState(initial = UiState.Loading)
    val query by viewModel.query.observeAsState(initial = "")

    val dataLoaded = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        if (!dataLoaded.value) {
            viewModel.getTopAnime()
            dataLoaded.value = true
        }
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
            HomeContent(
                animeList = (uiState as UiState.Success).data,
                modifier = modifier,
                navigateToDetail = navigateToDetail,
                navigateToFavorite= navigateToFavorite,
                searchQuery = query,
                onQueryChanged = viewModel::searchTopAnime
            )
        }

        is UiState.Error -> {
            val errorMessage = (uiState as UiState.Error).errorMessage
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    animeList: List<Anime>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    navigateToFavorite: () -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchField(
                searchQuery = searchQuery,
                onQueryChanged = onQueryChanged,
                modifier = Modifier.weight(1f)
            )

            OutlinedIconButton(
                onClick = navigateToFavorite,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(52.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_favorite_border_24),
                    contentDescription = "Favorite Page",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
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


@ExperimentalMaterial3Api
@Composable
fun SearchField(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    TextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        singleLine = true,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .indicatorLine(
                enabled = false,
                isError = false,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                focusedIndicatorLineThickness = 0.dp,
                unfocusedIndicatorLineThickness = 0.dp
            ),
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = ""
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun AnimeListItem(
    title: String,
    score: Double?,
    rank: Int?,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Log.d("AsyncImage", "Loading image from: $imageUrl")
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp, 100.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = "Score: ${score ?: "N/A"}")
                Text(text = "Rank: ${rank ?: "N/A"}")
            }
        }
    }
}