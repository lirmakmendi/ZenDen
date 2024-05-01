package com.sceproject.zenden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AppScaffold
import com.sceproject.zenden.components.HomeScreenContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    AppScaffold(
        title = stringResource(id = R.string.dashboard), // Use appropriate string resource
        homeViewModel = homeViewModel,
        secondaryViewModel = null,
        content = { paddingValues ->
            HomeScreenContent(paddingValues, homeViewModel) // Your existing content logic
        }
    )
}