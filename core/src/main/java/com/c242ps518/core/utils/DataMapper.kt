package com.c242ps518.core.utils

import com.c242ps518.core.data.source.local.entity.AnimeEntity
import com.c242ps518.core.data.source.remote.response.DataItem
import com.c242ps518.core.domain.model.Anime

object DataMapper {

    fun mapTopAnimeResponseToAnimeEntity(input: List<DataItem>): List<AnimeEntity> =
        input.map {
            AnimeEntity(
                animeId = it.malId,
                title = it.titleEnglish,
                score = it.score,
                rank = it.rank,
                imageUrl = it.images?.jpg?.imageUrl,
                synopsis = it.synopsis,
                genres = it.genres.joinToString(", ") { genre -> genre.name.toString() },
                isFavorite = false
            )
        }

    fun mapAnimeDomainToFavoriteAnimeEntity(input: Anime) = AnimeEntity(
        animeId = input.animeId,
        title = input.title ?: "",
        score = input.score ?: 0.0,
        rank = input.rank ?: 0,
        imageUrl = input.imageUrl ?: "",
        synopsis = input.synopsis ?: "",
        genres = input.genres ?: "",
        isFavorite = input.isFavorite
    )

    fun mapAnimeEntityToAnimeDomain(input: List<AnimeEntity>): List<Anime> =
        input.map {
            Anime(
                animeId = it.animeId,
                title = it.title,
                score = it.score,
                rank = it.rank,
                imageUrl = it.imageUrl,
                synopsis = it.synopsis,
                genres = it.genres,
                isFavorite = it.isFavorite
            )
        }
}