package com.c242ps518.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242ps518.core.data.repository.UiState
import com.c242ps518.core.domain.model.Anime
import com.c242ps518.core.domain.usecase.TopAnimeUseCase
import kotlinx.coroutines.launch


class FavoriteViewModel (private val topAnimeUseCase: TopAnimeUseCase) : ViewModel() {
    private val _uiState = MutableLiveData<UiState<List<Anime>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<Anime>>> = _uiState

    fun getFavoriteAnime() {
        viewModelScope.launch {
            try {
                topAnimeUseCase.getFavoriteAnime().collect { result ->
                    _uiState.postValue(UiState.Success(result))
                }
            } catch (e: Exception) {
                _uiState.postValue(UiState.Error(e.message.toString()))
            }
        }
    }
}