package com.c242ps518.core.data.source.local

import com.c242ps518.core.data.source.local.entity.AnimeEntity
import com.c242ps518.core.data.source.local.room.AnimeDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val animeDao: AnimeDao) {

    fun getAllAnime(): Flow<List<AnimeEntity>> = animeDao.getAllAnime()
    fun getAnimeById(animeId: Long): Flow<AnimeEntity> = animeDao.getAnimeById(animeId)
    fun getFavoriteAnime(): Flow<List<AnimeEntity>> = animeDao.getFavoriteAnime()
    fun searchAnime(query: String): Flow<List<AnimeEntity>> = animeDao.searchAnime(query)

    suspend fun insertAnime(anime: List<AnimeEntity>) = animeDao.insertAnime(anime)
    suspend fun updateFavoriteAnime(anime: AnimeEntity, newState: Boolean) {
        anime.isFavorite = newState
        animeDao.updateFavoriteAnime(anime)
    }
}