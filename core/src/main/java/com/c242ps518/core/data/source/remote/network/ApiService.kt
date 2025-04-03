package com.c242ps518.core.data.source.remote.network

import com.c242ps518.core.data.source.remote.response.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("limit") limit: Int = 20,
        @Query("filter") filter: String = "airing",
    ): TopAnimeResponse
}
