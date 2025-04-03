package com.c242ps518.topanimeairing.ui.screen.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242ps518.core.data.repository.UiState
import com.c242ps518.core.domain.model.Anime
import com.c242ps518.core.domain.usecase.TopAnimeUseCase
import kotlinx.coroutines.launch

class DetailViewModel (private val topAnimeUseCase: TopAnimeUseCase) : ViewModel() {
    private val _uiState = MutableLiveData<UiState<Anime>>(UiState.Loading)
    val uiState: LiveData<UiState<Anime>>
        get() = _uiState

    fun getAnimeById(animeId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            topAnimeUseCase.getAnimeById(animeId).collect {
                _uiState.postValue(UiState.Success(it))
            }
        }
    }

    fun setFavoriteAnime(anime: Anime, newState: Boolean) {
        viewModelScope.launch {
            topAnimeUseCase.setFavoriteAnime(anime, newState)
            _uiState.value = UiState.Loading // Menandakan loading ulang
            _uiState.value = UiState.Success(anime.copy(isFavorite = newState))
        }
    }
}