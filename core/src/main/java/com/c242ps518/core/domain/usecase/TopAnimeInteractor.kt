package com.c242ps518.core.domain.usecase


import com.c242ps518.core.domain.model.Anime
import com.c242ps518.core.domain.repository.ITopAnimeRepository

class TopAnimeInteractor (private val topAnimeRepository: ITopAnimeRepository): TopAnimeUseCase {
    override fun getTopAnime() = topAnimeRepository.getTopAnime()

    override fun getAnimeById(animeId: Long) = topAnimeRepository.getAnimeById(animeId)

    override fun searchTopAnime(query: String) = topAnimeRepository.searchTopAnime(query)
    override fun getFavoriteAnime() = topAnimeRepository.getFavoriteAnime()

    override suspend fun setFavoriteAnime(anime: Anime, newState: Boolean) = topAnimeRepository.setFavoriteAnime(anime, newState)
}