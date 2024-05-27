package com.sceproject.zenden.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.components.MyProfileContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel

@Composable
fun MyProfileScreen(homeViewModel: HomeViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        homeViewModel.checkForActiveSession()
        homeViewModel.getUserData()
        homeViewModel.resetPasswordStatus.value = ""
    }

    AppScaffold(
        title = stringResource(id = R.string.my_profile), // Use appropriate string resource
        homeViewModel = homeViewModel,
        secondaryViewModel = null,
        content = { paddingValues ->
            MyProfileContent(paddingValues, homeViewModel)
        }
    )
}