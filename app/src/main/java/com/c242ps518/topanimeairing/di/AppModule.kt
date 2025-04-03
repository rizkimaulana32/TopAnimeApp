package com.c242ps518.topanimeairing.di

import com.c242ps518.core.domain.usecase.TopAnimeInteractor
import com.c242ps518.core.domain.usecase.TopAnimeUseCase
import com.c242ps518.topanimeairing.ui.screen.detail.DetailViewModel
import com.c242ps518.topanimeairing.ui.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<TopAnimeUseCase> { TopAnimeInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}