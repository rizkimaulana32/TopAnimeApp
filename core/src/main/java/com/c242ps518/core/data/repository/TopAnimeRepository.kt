package com.c242ps518.core.data.repository

import com.c242ps518.core.data.NetworkBoundResource
import com.c242ps518.core.data.source.local.LocalDataSource
import com.c242ps518.core.data.source.remote.RemoteDataSource
import com.c242ps518.core.data.source.remote.network.ApiResponse
import com.c242ps518.core.data.source.remote.response.DataItem
import com.c242ps518.core.domain.model.Anime
import com.c242ps518.core.domain.repository.ITopAnimeRepository
import com.c242ps518.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopAnimeRepository (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ITopAnimeRepository {
    override fun getTopAnime(): Flow<UiState<List<Anime>>> =
        object : NetworkBoundResource<List<Anime>, List<DataItem>>() {

            override fun loadFromDB(): Flow<List<Anime>> {
                return localDataSource.getAllAnime().map {
                    DataMapper.mapAnimeEntityToAnimeDomain(it)
                }
            }
            override fun shouldFetch(data: List<Anime>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<DataItem>>> =
                remoteDataSource.getTopAnime()

            override suspend fun saveCallResult(data: List<DataItem>) {
                val tourismList = DataMapper.mapTopAnimeResponseToAnimeEntity(data)
                localDataSource.insertAnime(tourismList)
            }
        }.asFlow()

    override fun getAnimeById(animeId: Long): Flow<Anime> {
        return localDataSource.getAnimeById(animeId).map {
            DataMapper.mapAnimeEntityToAnimeDomain(listOf(it)).first()
        }
    }

    override fun searchTopAnime(query: String): Flow<List<Anime>> {
        return localDataSource.searchAnime(query).map {
            DataMapper.mapAnimeEntityToAnimeDomain(it)
        }
    }

    override fun getFavoriteAnime(): Flow<List<Anime>> {
        return localDataSource.getFavoriteAnime().map {
            DataMapper.mapAnimeEntityToAnimeDomain(it)
        }
    }

    override suspend fun setFavoriteAnime(anime: Anime, newState: Boolean) {
        val animeEntity = DataMapper.mapAnimeDomainToFavoriteAnimeEntity(anime)
        localDataSource.updateFavoriteAnime(animeEntity, newState)
    }
}