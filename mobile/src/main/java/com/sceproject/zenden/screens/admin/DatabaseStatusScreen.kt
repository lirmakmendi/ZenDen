package com.sceproject.zenden.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AdminScaffold
import com.sceproject.zenden.components.RelaxationContent
import com.sceproject.zenden.components.admin.DatabaseStatusContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel

@Composable
fun DatabaseStatusScreen(homeViewModel: HomeViewModel = viewModel()) {
    // Clear the database status when the screen is first displayed
    LaunchedEffect(key1 = true) {
        homeViewModel.isDatabaseAlive.value = null
    }

    AdminScaffold(
        title = stringResource(id = R.string.database_status), // Use appropriate string resource
        homeViewModel = homeViewModel,
        secondaryViewModel = null,
        content = { paddingValues ->
            DatabaseStatusContent(paddingValues, homeViewModel) // Your existing content logic
        }
    )
}
