package com.c242ps518.core.data

import com.c242ps518.core.data.source.remote.network.ApiResponse
import com.c242ps518.core.data.repository.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType : Any, RequestType> {

    private var result: Flow<UiState<ResultType>> = flow {
        emit(UiState.Loading)
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(UiState.Loading)
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { UiState.Success(it) })
                }
                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { UiState.Success(it) })
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(UiState.Error(apiResponse.errorMessage))
                }
            }
        } else {
            emitAll(loadFromDB().map { UiState.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<UiState<ResultType>> = result
}