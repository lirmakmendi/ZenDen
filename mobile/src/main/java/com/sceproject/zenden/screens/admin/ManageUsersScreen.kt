package com.sceproject.zenden.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.AdminScaffold
import com.sceproject.zenden.components.RelaxationContent
import com.sceproject.zenden.components.admin.ManageUsersContent
import com.sceproject.zenden.data.viewmodels.HomeViewModel
import com.sceproject.zenden.navigation.Screen

@Composable
fun ManageUsersScreen(homeViewModel: HomeViewModel = viewModel()) {
    AdminScaffold(
        title = stringResource(id = R.string.manage_users), // Use appropriate string resource
        homeViewModel = homeViewModel,
        secondaryViewModel = null,
        content = { paddingValues ->
            ManageUsersContent(paddingValues, homeViewModel)
        }
    )
}
