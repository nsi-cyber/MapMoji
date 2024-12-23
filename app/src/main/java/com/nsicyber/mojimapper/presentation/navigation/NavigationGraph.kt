package com.nsicyber.mojimapper.presentation.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nsicyber.mojimapper.common.Constants
import com.nsicyber.mojimapper.presentation.MainEvent
import com.nsicyber.mojimapper.presentation.MainViewModel
import com.nsicyber.mojimapper.presentation.cameraScreen.CameraScreen
import com.nsicyber.mojimapper.presentation.mapScreen.MapScreen

@Composable
fun NavigationGraph(
    applicationContext: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel<MainViewModel>(),
    startDestination: String = Constants.Destination.MAP_SCREEN,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    },
    requestCameraPermission: () -> Unit,
    requestLocationPermission: () -> Unit,
) {

    LaunchedEffect(Unit) {
        mainViewModel.onEvent(MainEvent.DeleteOldData)
    }



    val isMenuShow = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route




    Scaffold { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding),
        ) {


            composable(route = Constants.Destination.MAP_SCREEN) {
                MapScreen(
                    mainViewModel = mainViewModel,
                    applicationContext = applicationContext,
                    onLocationRequest = requestLocationPermission,
                    onCameraPage = { navActions.navigateToCameraScreen() }
                )
            }
            composable(route = Constants.Destination.CAMERA_SCREEN) {
                CameraScreen(
                    mainViewModel = mainViewModel,
                    applicationContext = applicationContext,
                    onLocationRequest = requestLocationPermission,
                    onBackPressed = { navActions.popBackStack() }
                )
            }


        }


    }
}