package com.c242ps518.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Anime (
    val animeId: Long,
    val title: String?,
    val score: Double?,
    val rank: Int?,
    val imageUrl: String?,
    val synopsis: String?,
    val genres: String?,
    val isFavorite: Boolean
) : Parcelable