package com.sceproject.zenden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.OfflineAnswerPDSSContent
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.components.OfflineAppScaffold
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.data.viewmodels.OfflineAnswerPDSSViewModel
import com.sceproject.zenden.navigation.Screen
import com.sceproject.zenden.navigation.ZenDenAppRouter

@Composable
fun OfflineAnswerPDSSScreen(homeViewModel: HomeViewModel = viewModel(), offlineAnswerPDSSViewModel: OfflineAnswerPDSSViewModel = viewModel()) {
    OfflineAppScaffold(
        title = "ענה על שאלון לא מקוון",
        homeViewModel = homeViewModel,
        secondaryViewModel = offlineAnswerPDSSViewModel,
        enableMenu = false, // Disable the menu for this screen
        onBackClicked = { ZenDenAppRouter.navigateTo(Screen.LoginScreen) },
        content = { paddingValues ->
            OfflineAnswerPDSSContent(paddingValues, offlineAnswerPDSSViewModel)
        }
    )
}