package com.c242ps518.core.data.source.remote

import com.c242ps518.core.data.source.remote.network.ApiResponse
import com.c242ps518.core.data.source.remote.response.DataItem
import com.c242ps518.core.data.source.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource (private val apiService: ApiService){

    fun getTopAnime(): Flow<ApiResponse<List<DataItem>>> = flow {
        try {
            val response = apiService.getTopAnime()
            val uniqueAnimeList = response.data.filter { it.titleEnglish != null }.distinctBy { it.malId }
            emit(ApiResponse.Success(uniqueAnimeList))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

}