package com.sceproject.zenden.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.screens.AnxietyTrendsScreen
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter
import com.sceproject.zenden.screens.HomeScreen
import com.sceproject.zenden.screens.LoginScreen
import com.sceproject.zenden.screens.MeasureNowScreen
import com.sceproject.zenden.screens.RelaxationTechniquesScreen
import com.sceproject.zenden.screens.SignUpScreen
import com.sceproject.zenden.screens.TermsAndConditionsScreen

@Composable
fun ZenDen(homeViewModel: HomeViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        if (homeViewModel.isUserLoggedIn.value == true) {
            ZenDenAppRouter.navigateTo(Screen.HomeScreen)
        }

        Crossfade(targetState = ZenDenAppRouter.currentScreen) { currentState ->
            when (currentState.value) {
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }

                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }

                is Screen.LoginScreen -> {
                    LoginScreen()
                }

                is Screen.HomeScreen -> {
                    HomeScreen()
                }

                is Screen.RelaxationScreen -> {
                    RelaxationTechniquesScreen()
                }

                is Screen.AnxietyTrendsScreen -> {
                    AnxietyTrendsScreen()
                }

                is Screen.MeasureNowScreen -> {
                    MeasureNowScreen()
                }


            }
        }

    }
}