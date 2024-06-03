package com.sceproject.zenden.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    object SignUpScreen : Screen()
    object TermsAndConditionsScreen : Screen()
    object LoginScreen : Screen()
    object HomeScreen : Screen()
    object RelaxationScreen : Screen()
    object AnxietyTrendsScreen : Screen()
    object MeasureNowScreen : Screen()
    object MyProfileScreen : Screen()
    object AdminScreen : Screen()
    object ManageUsersScreen : Screen()
    object DatabaseStatusScreen : Screen()
    object PanicAttackInfoScreen : Screen()
}


object ZenDenAppRouter {

    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }


}