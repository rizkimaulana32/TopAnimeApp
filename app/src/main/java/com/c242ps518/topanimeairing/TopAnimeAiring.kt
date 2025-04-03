package com.c242ps518.topanimeairing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.c242ps518.topanimeairing.ui.navigation.NavigateDf
import com.c242ps518.topanimeairing.ui.navigation.Screen
import com.c242ps518.topanimeairing.ui.screen.detail.DetailScreen
import com.c242ps518.topanimeairing.ui.screen.home.HomeScreen

@Composable
fun TopAnimeAiring(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    },
                    navigateToFavorite = {
                        navController.navigate(Screen.Favorite.route)
                    }
                )
            }

            composable(
                Screen.Detail.route,
                arguments = listOf(navArgument("animeId") { type = NavType.LongType }),
            ) {
                val id = it.arguments?.getLong("animeId") ?: -1L
                DetailScreen(
                    animeId = id,
                    navigateBack = {
                        navController.navigateUp()
                    },
                )
            }

//            composable(Screen.Favorite.route){
//                com.c242ps518.favorite.FavoriteScreen(
//                    navigateToDetail = { animeId ->
//                        navController.navigate(Screen.Detail.createRoute(animeId))
//                    },
//                    navigateBack = {
//                        navController.navigateUp()
//                    }
//                )
//            }

            composable(Screen.Favorite.route){
                NavigateDf.loadDF(
                    paddingValues = innerPadding,
                    className = "com.c242ps518.favorite.FavoriteScreenKt",
                    methodName = "FavoriteScreen",
                    navigateToDetail = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
