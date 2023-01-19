package com.example.jetutils_library

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.jetutils_library.uiPermission.PermissionShowOff
import com.example.jetutils_library.uiTextSample.UiTextShowOff
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lib.jetutils.composeUtils.CountryCodeDialog
import com.lib.jetutils.composeUtils.CustomCountryCodeDialog

object Routes {
    const val MainScreen = "/mainScreen"
    const val UiTextScreen = "/uiText"
    const val Permission = "/permissionDialog"
    const val CountryCodeDialog = "/countryCodeDialog"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController = rememberAnimatedNavController(),
) {
    AnimatedNavHost(navController = navController, startDestination = Routes.CountryCodeDialog) {
        composable(Routes.MainScreen) {
            MainScreen(navController)
        }
        composable(Routes.UiTextScreen) {
            UiTextShowOff(navController)
        }
        composable(Routes.Permission) {
            PermissionShowOff(navController)
        }
        composable(Routes.CountryCodeDialog) {
            CustomCountryCodeDialog(onDismiss = {}) {

            }
        }
    }

}
