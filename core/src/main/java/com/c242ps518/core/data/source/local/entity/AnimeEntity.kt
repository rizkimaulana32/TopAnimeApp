package com.c242ps518.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val animeId: Long,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "score")
    val score: Double?,

    @ColumnInfo(name = "rank")
    val rank: Int?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "synopsis")
    val synopsis: String?,

    @ColumnInfo(name = "genres")
    val genres: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
)