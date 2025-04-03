package com.c242ps518.topanimeairing.ui.navigation

sealed class Screen (val route: String){
    data object Home : Screen("home")
    data object Detail : Screen("home/{animeId}"){
        fun createRoute(animeId: Long) = "home/$animeId"
    }
    data object Favorite : Screen("favorite")
}