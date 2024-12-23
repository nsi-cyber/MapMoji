package com.nsicyber.mojimapper.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.nsicyber.mojimapper.common.Constants

class NavigationActions(private val navController: NavHostController) {



    fun navigateToCameraScreen() {
        navController.navigate(Constants.Destination.CAMERA_SCREEN) {
            popUpToTop(navController)
        }
    }


    fun popBackStack() {
        navController.popBackStack()
    }


}

fun NavOptionsBuilder.popUpToTop(navController: NavController, clean:Boolean=false) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = clean
    }
}
