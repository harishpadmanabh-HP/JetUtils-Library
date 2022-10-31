package com.example.jetutils_library

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.jetutils_library.uiTextSample.UiTextShowOff
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

object Routes {
    const val MainScreen = "/mainScreen"
    const val UiTextScreen = "/uiText"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    AnimatedNavHost(navController = navController, startDestination = Routes.MainScreen) {
        composable(Routes.MainScreen){
            MainScreen(navController)
        }
        composable(Routes.UiTextScreen){
            UiTextShowOff(navController)
        }
    }

}
