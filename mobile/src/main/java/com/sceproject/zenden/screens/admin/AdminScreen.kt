package com.sceproject.zenden.screens.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sceproject.zenden.R
import com.sceproject.zenden.components.admin.AdminContent
import com.sceproject.zenden.components.AdminScaffold
import com.sceproject.zenden.data.viewmodels.HomeViewModel
@Composable

fun AdminScreen(homeViewModel: HomeViewModel = viewModel()) {
    val statistics = getFakeStatistics()
    AdminScaffold(
        title = stringResource(id = R.string.admin_dashboard), // Use appropriate string resource
        homeViewModel = homeViewModel,
        secondaryViewModel = null,
        content = { paddingValues ->
            AdminContent(paddingValues, statistics)
        }
    )
}


data class Statistics(
    val totalUsers: Int,
    val activeUsers: Int,
    val inactiveUsers: Int,
    val newUsersThisMonth: Int,
    val totalPosts: Int
)

fun getFakeStatistics(): Statistics {
    return Statistics(
        totalUsers = 1200,
        activeUsers = 900,
        inactiveUsers = 300,
        newUsersThisMonth = 50,
        totalPosts = 3500
    )
}
