package com.c242ps518.core.domain.repository

import com.c242ps518.core.data.repository.UiState
import com.c242ps518.core.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface ITopAnimeRepository {
    fun getTopAnime(): Flow<UiState<List<Anime>>>
    fun getAnimeById(animeId: Long): Flow<Anime>
    fun searchTopAnime(query: String): Flow<List<Anime>>
    fun getFavoriteAnime(): Flow<List<Anime>>
    suspend fun setFavoriteAnime(anime: Anime, newState: Boolean)
}